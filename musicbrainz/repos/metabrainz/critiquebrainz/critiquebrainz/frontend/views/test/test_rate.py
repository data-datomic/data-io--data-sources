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

from unittest import mock
from flask import url_for
from critiquebrainz.frontend.testing import FrontendTestCase
import critiquebrainz.frontend.external.musicbrainz_db.release_group as mb_release_group
import critiquebrainz.db.users as db_users
from critiquebrainz.db.user import User
import critiquebrainz.db.review as db_review
import critiquebrainz.db.license as db_license


class RateViewsTestCase(FrontendTestCase):

    def setUp(self):
        super(RateViewsTestCase, self).setUp()
        self.test_entity = {
            'id': 'e7aad618-fa86-3983-9e77-405e21796eca',
            'title': 'Test Release Group',
            'first-release-year': 1970,
        }
        mb_release_group.get_release_group_by_id = mock.MagicMock()
        mb_release_group.get_release_group_by_id.return_value = self.test_entity
        self.reviewer = User(db_users.get_or_create(1, "aef06569-098f-4218-a577-b413944d9493", new_user_data={
            "display_name": u"Reviewer",
        }))
        self.license = db_license.create(
            id="CC BY-SA 3.0",
            full_name="Test License.",
        )

    def test_rate(self):
        self.temporary_login(self.reviewer)

        # Test for first time rating (no review exists)
        payload = {
            'entity_id': self.test_entity['id'],
            'entity_type': 'release_group',
            'rating': 4
        }
        response = self.client.post(
            url_for('rate.rate'),
            data=payload
        )

        self.assertRedirects(response, '/release-group/{}'.format(self.test_entity['id']))

        reviews, review_count = db_review.list_reviews(
            entity_id=self.test_entity['id'],
            entity_type='release_group',
            user_id=self.reviewer.id
        )
        # Test that the rate request created a review
        self.assertEqual(review_count, 1)
        review = reviews[0]
        self.assertEqual(review['text'], None)
        self.assertEqual(review['rating'], 4)

        response = self.client.get('/release-group/{}'.format(self.test_entity['id']))
        self.assert200(response)
        self.assertIn('We have updated your rating for this entity!', str(response.data))

        # Test after rating is cleared for review with NO text
        payload = {
            'entity_id': self.test_entity['id'],
            'entity_type': 'release_group',
            'rating': None
        }
        response = self.client.post(
            url_for('rate.rate'),
            data=payload
        )

        reviews, review_count = db_review.list_reviews(
            entity_id=self.test_entity['id'],
            entity_type='release_group',
            user_id=self.reviewer.id
        )
        # Test that the clear rating request results in deletion of review (because review-text was None)
        self.assertEqual(review_count, 0)

        # Test after rating is cleared for review with some text
        self.review = db_review.create(
            user_id=self.reviewer.id,
            entity_id=self.test_entity['id'],
            entity_type="release_group",
            text="Test Review.",
            rating=4,
            is_draft=False,
            license_id=self.license["id"],
        )

        payload = {
            'entity_id': self.test_entity['id'],
            'entity_type': 'release_group',
            'rating': None
        }
        response = self.client.post(
            url_for('rate.rate'),
            data=payload
        )

        reviews, review_count = db_review.list_reviews(
            entity_id=self.test_entity['id'],
            entity_type='release_group',
            user_id=self.reviewer.id
        )
        # Test that the clear rating request doesn't delete review (because review-text was not None)
        self.assertEqual(review_count, 1)
        review = reviews[0]
        self.assertEqual(review['rating'], None)
