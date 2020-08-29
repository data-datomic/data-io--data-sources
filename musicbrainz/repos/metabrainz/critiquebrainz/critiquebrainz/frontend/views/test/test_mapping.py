# critiquebrainz - Repository for Creative Commons licensed reviews
#
# Copyright (C) 2018 Bimalkant Lauhny.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

from unittest.mock import MagicMock
from flask import url_for
import critiquebrainz.db.users as db_users
from critiquebrainz.db.user import User
from critiquebrainz.frontend.testing import FrontendTestCase
from critiquebrainz.frontend.external import mbspotify
import critiquebrainz.frontend.external.spotify as spotify_api
from critiquebrainz.frontend.external.exceptions import ExternalServiceException
import critiquebrainz.frontend.external.musicbrainz_db.exceptions as mb_exceptions
import critiquebrainz.frontend.external.musicbrainz_db.release_group as mb_release_group


class SpotifyMappingViewsTestCase(FrontendTestCase):

    def setUp(self):
        super(SpotifyMappingViewsTestCase, self).setUp()
        self.user = User(db_users.get_or_create(1, "aef06569-098f-4218-a577-b413944d9493", new_user_data={
            "display_name": u"Tester",
        }))

        self.test_spotify_id = "6IH6co1QUS7uXoyPDv0rIr"
        self.test_release_group = {
            'id': '6b3cd75d-7453-39f3-86c4-1441f360e121',
            'title': 'Test Release Group',
            'first-release-year': 1970,
            'artist-credit': [{
                'name': 'Test Artist'
            }]
        }
        self.test_spotify_get_multiple_albums_response = {
            '6IH6co1QUS7uXoyPDv0rIr': {
                'type': 'album',
                'album_type': 'album',
                'id': '6IH6co1QUS7uXoyPDv0rIr',
                'name': 'Test Album',
                'release_date': '1970-01-01',
                'external_urls': {
                    'spotify': 'https://open.spotify.com/album/6IH6co1QUS7uXoyPDv0rIr'
                },
                'artists': [{'name': 'Test Artist'}],
                'tracks': {
                    'items': [{
                        'artists': [{
                            'name': 'Test Artist'
                        }]
                    }]
                },
                'uri': 'spotify:album:6IH6co1QUS7uXoyPDv0rIr'
            }
        }

        self.test_spotify_search_response = {
            'albums': {
                'items': [{
                    'id': "6IH6co1QUS7uXoyPDv0rIr"
                }],
                'total': 1
            }
        }

    def test_spotify_list(self):
        # test for non-existent release group
        mbspotify.mappings = MagicMock(return_value=[])
        mb_release_group.get_release_group_by_id = MagicMock(side_effect=mb_exceptions.NoDataFoundException)
        response = self.client.get("/mapping/6b3cd75d-7453-39f3-86c4-1441f360e121")
        self.assert404(response, "Can't find release group with a specified ID.")

        # test for release group with no mappings
        mb_release_group.get_release_group_by_id = MagicMock(return_value=self.test_release_group)
        response = self.client.get("/mapping/6b3cd75d-7453-39f3-86c4-1441f360e121")
        self.assert200(response)
        self.assertIn("No mappings", str(response.data))

        # test release group with mappings
        mbspotify.mappings = MagicMock(return_value=['spotify:album:6IH6co1QUS7uXoyPDv0rIr'])
        spotify_api.get_multiple_albums = MagicMock(return_value=self.test_spotify_get_multiple_albums_response)
        response = self.client.get("/mapping/6b3cd75d-7453-39f3-86c4-1441f360e121")
        self.assert200(response)
        self.assertIn(self.test_spotify_id, str(response.data))
        self.assertIn("Test Album", str(response.data))
        self.assertIn("1970-01-01", str(response.data))

        # test spotify service unavailable
        spotify_api.get_multiple_albums = MagicMock(side_effect=ExternalServiceException)
        response = self.client.get("/mapping/6b3cd75d-7453-39f3-86c4-1441f360e121")
        self.assertStatus(response, 503)

    def test_spotify_add(self):
        # test `release_group_id` variable not supplied
        response = self.client.get("/mapping/spotify/add")
        self.assertRedirects(response, url_for('frontend.index'))

        # test for non-existent release group
        mb_release_group.get_release_group_by_id = MagicMock(side_effect=mb_exceptions.NoDataFoundException)
        response = self.client.get("/mapping/spotify/add",
                                   query_string={"release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"},
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("Only existing release groups can be mapped to Spotify!", str(response.data))

        # test Spotify service unavailable
        mb_release_group.get_release_group_by_id = MagicMock(return_value=self.test_release_group)
        spotify_api.search = MagicMock(side_effect=ExternalServiceException)
        response = self.client.get("/mapping/spotify/add",
                                   query_string={"release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"})
        self.assertStatus(response, 503)

        # test when response has no albums for given id
        spotify_api.search = MagicMock(return_value=self.test_spotify_search_response)

        spotify_api.get_multiple_albums = MagicMock(return_value={})
        response = self.client.get("/mapping/spotify/add",
                                   query_string={"release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"})
        self.assert200(response)
        self.assertIn("No similar albums found", str(response.data))

        # test when response has 1 album
        spotify_api.get_multiple_albums = MagicMock(return_value=self.test_spotify_get_multiple_albums_response)
        response = self.client.get("/mapping/spotify/add",
                                   query_string={"release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"})
        self.assert200(response)
        self.assertIn("Listen on Spotify", str(response.data))
        self.assertIn("1970", str(response.data))
        self.assertIn("Test Album", str(response.data))
        self.assertIn("Test Artist", str(response.data))

    def test_spotify_confirm(self):
        self.temporary_login(self.user)

        # test `release_group_id` variable not supplied
        response = self.client.get("/mapping/spotify/confirm",
                                   follow_redirects=True)
        self.assert400(response, "Didn't provide `release_group_id`!")

        # test for non-existent release group
        mb_release_group.get_release_group_by_id = MagicMock(side_effect=mb_exceptions.NoDataFoundException)
        response = self.client.get("/mapping/spotify/confirm",
                                   query_string={"release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"},
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("Only existing release groups can be mapped to Spotify!", str(response.data))

        # test `spotify_ref` variable not supplied
        mb_release_group.get_release_group_by_id = MagicMock(return_value=self.test_release_group)
        response = self.client.get("/mapping/spotify/confirm",
                                   query_string={"release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"},
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("You need to select an album from Spotify!", str(response.data))

        # test when wrong type of `spotify_ref` is supplied
        response = self.client.get("/mapping/spotify/confirm",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_ref": "Unsupported Spotify URI"
                                   },
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("You need to specify a correct link to this album on Spotify!", str(response.data))

        # test Spotify service unavailable or uri supplied is not available on Spotify
        spotify_api.get_album = MagicMock(side_effect=ExternalServiceException)
        response = self.client.get("/mapping/spotify/confirm",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_ref": "spotify:album:6IH6co1QUS7uXoyPDv0rIr"
                                   },
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("You need to specify existing album from Spotify!", str(response.data))

        # test when uri supplied is available on Spotify
        spotify_api.get_album = MagicMock(return_value=self.test_spotify_get_multiple_albums_response[self.test_spotify_id])
        response = self.client.get("/mapping/spotify/confirm",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_ref": "spotify:album:6IH6co1QUS7uXoyPDv0rIr"
                                   },
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("Spotify album mapping confirmation", str(response.data))
        self.assertIn("Are you sure you want to create this mapping?", str(response.data))

        # test POST for spotify_confirm
        mbspotify.add_mapping = MagicMock(return_value=(False, None))

        # test when failed to add mapping while posting uri
        response = self.client.post("/mapping/spotify/confirm",
                                    query_string={
                                        "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                        "spotify_ref": "spotify:album:6IH6co1QUS7uXoyPDv0rIr"
                                    },
                                    follow_redirects=True)
        self.assert200(response)
        self.assertIn("Could not add Spotify mapping!", str(response.data))

        # test when successfully added mapping
        mbspotify.add_mapping = MagicMock(return_value=(True, None))
        response = self.client.post("/mapping/spotify/confirm",
                                    query_string={
                                        "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                        "spotify_ref": "spotify:album:6IH6co1QUS7uXoyPDv0rIr"
                                    },
                                    follow_redirects=True)
        self.assert200(response)
        self.assertIn("Spotify mapping has been added!", str(response.data))

    def test_spotify_report(self):
        self.temporary_login(self.user)

        # test `release_group_id` variable not supplied
        response = self.client.get("/mapping/spotify/report",
                                   follow_redirects=True)
        self.assert400(response, "Didn't provide `release_group_id`!")

        # test `spotify_id` variable not supplied
        response = self.client.get("/mapping/spotify/report",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121"
                                   },
                                   follow_redirects=True)
        self.assert400(response, "Didn't provide `spotify_id`!")

        # test for non-existent release group
        mb_release_group.get_release_group_by_id = MagicMock(side_effect=mb_exceptions.NoDataFoundException)
        response = self.client.get("/mapping/spotify/report",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_id": "6IH6co1QUS7uXoyPDv0rIr"
                                   },
                                   follow_redirects=True)
        self.assert404(response, "Can't find release group with a specified ID.")

        # test release group not mapped to supplied spotify uri
        mb_release_group.get_release_group_by_id = MagicMock(return_value=self.test_release_group)
        mbspotify.mappings = MagicMock(return_value=[])
        response = self.client.get("/mapping/spotify/report",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_id": "6IH6co1QUS7uXoyPDv0rIr"
                                   },
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("This album is not mapped to Spotify yet!", str(response.data))

        # test when release group is mapped to supplied spotify uri, but, album for supplied uri doesn't exist
        mbspotify.mappings = MagicMock(return_value=["spotify:album:6IH6co1QUS7uXoyPDv0rIr"])
        spotify_api.get_album = MagicMock(side_effect=ExternalServiceException)
        spotify_api.get_multiple_albums = MagicMock(return_value=self.test_spotify_get_multiple_albums_response)
        response = self.client.get("/mapping/spotify/report",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_id": "6IH6co1QUS7uXoyPDv0rIr"
                                   },
                                   follow_redirects=True)
        # self.assertRedirects(response, url_for('mapping.spotify_list', release_group_id=self.test_release_group['id']))
        self.assert200(response)
        self.assertIn("You need to specify existing album from Spotify!", str(response.data))

        # test confirmation page for reporting
        spotify_api.get_album = MagicMock(return_value=self.test_spotify_get_multiple_albums_response[self.test_spotify_id])
        response = self.client.get("/mapping/spotify/report",
                                   query_string={
                                       "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                       "spotify_id": "6IH6co1QUS7uXoyPDv0rIr"
                                   },
                                   follow_redirects=True)
        self.assert200(response)
        self.assertIn("Are you sure you want to report incorrect mapping?", str(response.data))

        # test POST for spotify_report
        # test when successfully added mapping
        mbspotify.vote = MagicMock(return_value=(True, None))
        response = self.client.post("/mapping/spotify/report",
                                    query_string={
                                        "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                        "spotify_id": "6IH6co1QUS7uXoyPDv0rIr"
                                    },
                                    follow_redirects=True)
        self.assert200(response)
        self.assertIn("Incorrect Spotify mapping has been reported. Thank you!", str(response.data))

        # test when failed to vote for incorrect mapping
        mbspotify.vote = MagicMock(return_value=(False, None))
        response = self.client.post("/mapping/spotify/report",
                                    query_string={
                                        "release_group_id": "6b3cd75d-7453-39f3-86c4-1441f360e121",
                                        "spotify_id": "6IH6co1QUS7uXoyPDv0rIr"
                                    },
                                    follow_redirects=True)
        self.assert200(response)
        self.assertIn("Could not report incorrect Spotify mapping!", str(response.data))
