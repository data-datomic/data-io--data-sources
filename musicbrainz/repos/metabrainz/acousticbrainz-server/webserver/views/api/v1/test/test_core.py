from __future__ import absolute_import

import collections
import json
import os
import unittest
import uuid

import mock

import db.exceptions
import webserver.views.api.exceptions
from webserver.testing import AcousticbrainzTestCase
from webserver.testing import DB_TEST_DATA_PATH
from webserver.views.api.v1 import core


class CoreViewsTestCase(AcousticbrainzTestCase):

    def setUp(self):
        super(CoreViewsTestCase, self).setUp()
        self.uuid = str(uuid.uuid4())

        self.test_recording1_mbid = '0dad432b-16cc-4bf0-8961-fd31d124b01b'
        self.test_recording1_data_json = open(os.path.join(DB_TEST_DATA_PATH, self.test_recording1_mbid + '.json')).read()
        self.test_recording1_data = json.loads(self.test_recording1_data_json)

        self.test_recording2_mbid = 'e8afe383-1478-497e-90b1-7885c7f37f6e'
        self.test_recording2_data_json = open(os.path.join(DB_TEST_DATA_PATH, self.test_recording2_mbid + '.json')).read()
        self.test_recording2_data = json.loads(self.test_recording2_data_json)

    def test_get_low_level(self):
        mbid = "0dad432b-16cc-4bf0-8961-fd31d124b01b"
        resp = self.client.get("/api/v1/%s/low-level" % mbid)
        self.assertEqual(resp.status_code, 404)

        self.load_low_level_data(mbid)

        resp = self.client.get("/api/v1/%s/low-level" % mbid)
        self.assertEqual(resp.status_code, 200)
        self.assertIn('X-RateLimit-Limit', resp.headers)
        self.assertIn('X-RateLimit-Remaining', resp.headers)
        self.assertIn('X-RateLimit-Reset-In', resp.headers)
        self.assertIn('X-RateLimit-Reset', resp.headers)

        # upper-case MBID
        resp = self.client.get("/api/v1/%s/low-level" % mbid.upper())
        self.assertEqual(resp.status_code, 200)

    def test_submit_low_level(self):
        mbid = "0dad432b-16cc-4bf0-8961-fd31d124b01b"

        with open(os.path.join(DB_TEST_DATA_PATH, mbid + ".json")) as json_file:
            with self.app.test_client() as client:
                sub_resp = client.post("/api/v1/%s/low-level" % mbid,
                                       data=json_file.read(),
                                       content_type="application/json")
                self.assertEqual(sub_resp.status_code, 200)

        resp = self.client.get("/api/v1/%s/low-level" % mbid)
        self.assertEqual(resp.status_code, 200)

    def test_cors_headers(self):
        mbid = "0dad432b-16cc-4bf0-8961-fd31d124b01b"
        self.load_low_level_data(mbid)

        for req in ["%s/low-level", "low-level?recording_ids=%s"]:
            resp = self.client.get("/api/v1/" + req % mbid)
            self.assertEqual(resp.headers["Access-Control-Allow-Origin"], "*")

        # TODO: Test in get_high_level.

    @mock.patch("db.data.load_low_level")
    def test_ll_bad_uuid_404(self, load_low_level):
        """ URL Endpoint returns 404 because url-part doesn't match UUID.
            This error is raised by Flask, but we special-case to json.
        """
        resp = self.client.get("/api/v1/nothing/low-level")
        self.assertEqual(404, resp.status_code)
        load_low_level.assert_not_called()

        expected_result = {"message": "The requested URL was not found on the server. If you entered the URL manually please check your spelling and try again."}
        self.assertEqual(resp.json, expected_result)

    @mock.patch("db.data.load_low_level")
    def test_ll_internal_server_error(self, load_low_level):

        # Flask will propagate exceptions instead of calling an error handler
        # if either DEBUG *or* TESTING is True. In order to actually test
        # that a programming error in the API results in a nice error message
        # being set, we temporarily disable it
        old_propagate_exceptions = self.app.config['PROPAGATE_EXCEPTIONS']
        self.app.config['PROPAGATE_EXCEPTIONS'] = False

        load_low_level.side_effect = ValueError
        resp = self.client.get("/api/v1/%s/low-level" % self.uuid)
        self.assertEqual(500, resp.status_code)

        expected_result = {"message": "An unknown error occurred"}
        self.assertDictEqual(resp.json, expected_result)
        self.app.config['PROPAGATE_EXCEPTIONS'] = old_propagate_exceptions

    @mock.patch("db.data.load_low_level")
    def test_ll_no_offset(self, ll):
        ll.return_value = {}
        resp = self.client.get("/api/v1/%s/low-level" % self.uuid)
        self.assertEqual(200, resp.status_code)
        ll.assert_called_with(self.uuid, 0)

    @mock.patch("db.data.load_low_level")
    def test_ll_numerical_offset(self, ll):
        ll.return_value = {}
        resp = self.client.get("/api/v1/%s/low-level?n=3" % self.uuid)
        self.assertEqual(200, resp.status_code)
        ll.assert_called_with(self.uuid, 3)

    @mock.patch("db.data.load_low_level")
    def test_ll_bad_offset(self, ll):
        # non-numerical offset is replaced by 0
        ll.return_value = {}
        resp = self.client.get("/api/v1/%s/low-level?n=x" % self.uuid)
        self.assertEqual(200, resp.status_code)
        ll.assert_called_with(self.uuid, 0)

    @mock.patch("db.data.load_low_level")
    def test_ll_no_item(self, ll):
        ll.side_effect = db.exceptions.NoDataFoundException
        resp = self.client.get("/api/v1/%s/low-level" % self.uuid)
        self.assertEqual(404, resp.status_code)
        self.assertEqual("Not found", resp.json["message"])

    @mock.patch("db.data.load_high_level")
    def test_get_high_level(self, hl):
        hl.return_value = {}
        resp = self.client.get("/api/v1/%s/high-level" % self.uuid)
        self.assertEqual(200, resp.status_code)
        hl.assert_called_with(self.uuid, 0, False)

        # upper-case
        resp = self.client.get("/api/v1/%s/high-level" % self.uuid.upper())
        self.assertEqual(200, resp.status_code)
        hl.assert_called_with(self.uuid, 0, False)

    @mock.patch("db.data.load_high_level")
    def test_hl_numerical_offset(self, hl):
        hl.return_value = {}
        resp = self.client.get("/api/v1/%s/high-level?n=3" % self.uuid)
        self.assertEqual(200, resp.status_code)
        hl.assert_called_with(self.uuid, 3, False)

    @mock.patch('db.data.load_many_low_level')
    def test_get_bulk_ll_no_param(self, load_many_low_level):
        # No parameter in bulk lookup results in an error
        resp = self.client.get('api/v1/low-level')
        self.assertEqual(resp.status_code, 400)

        expected_result = {"message": "Missing `recording_ids` parameter"}
        self.assertEqual(resp.json, expected_result)

    @mock.patch('db.data.load_many_low_level')
    def test_get_bulk_ll(self, load_many_low_level):
        # Check that many items are returned, including two offsets of the
        # same mbid

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:3;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2;405a5ff4-7ee2-436b-95c1-90ce8a83b359:3"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        rec_7f = {"recording": "7f27d7a9-27f0-4663-9d20-2c9c40200e6d"}
        rec_40_2 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"}
        rec_40_3 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:3"}

        load_many_low_level.return_value = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "7f27d7a9-27f0-4663-9d20-2c9c40200e6d": {"3": rec_7f},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2, "3": rec_40_3}
        }

        resp = self.client.get('api/v1/low-level?recording_ids=' + params)
        self.assertEqual(resp.status_code, 200)

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "7f27d7a9-27f0-4663-9d20-2c9c40200e6d": {"3": rec_7f},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2, "3": rec_40_3},
            "mbid_mapping": {}
        }
        self.assertEqual(resp.json, expected_result)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                      ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 3),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 3)]
        load_many_low_level.assert_called_with(recordings)

        # upper-case
        params = "c5f4909e-1d7b-4f15-a6f6-1AF376BC01C9"
        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5}
        }
        load_many_low_level.return_value = expected_result
        resp = self.client.get('api/v1/low-level?recording_ids=' + params.upper())
        self.assertEqual(resp.status_code, 200)

        # We expect that we get returned whatever we set the mock to, but the argument
        # to load_many_high_level is always lower-case regardless of what we pass in
        self.assertEqual(resp.json, expected_result)
        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0)]
        load_many_low_level.assert_called_with(recordings)

    @mock.patch('db.data.load_many_low_level')
    def test_get_bulk_ll_absent_mbid(self, load_many_low_level):
        # Check that within a set of mbid parameters, the ones absent
        # from the database are ignored.

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:3;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        rec_40_2 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"}

        load_many_low_level.return_value = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2}
        }

        resp = self.client.get('api/v1/low-level?recording_ids=' + params)
        self.assertEqual(resp.status_code, 200)

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2},
            "mbid_mapping": {}
        }
        self.assertEqual(resp.json, expected_result)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                      ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 3),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2)]
        load_many_low_level.assert_called_with(recordings)

    def test_get_bulk_ll_more_than_25(self):
        # Create many random uuids, because of parameter deduplication
        manyids = [str(uuid.uuid4()) for i in range(26)]
        limit_exceed_url = ";".join(manyids)
        resp = self.client.get('api/v1/low-level?recording_ids=' + limit_exceed_url)
        self.assertEqual(resp.status_code, 400)
        self.assertEqual('More than 25 recordings not allowed per request', resp.json['message'])

    @mock.patch('db.data.load_high_level')
    def test_get_bulk_hl_no_param(self, load_high_level):
        # No parameter in bulk lookup results in an error
        resp = self.client.get('api/v1/high-level')
        self.assert400(resp)

        expected_result = {"message": "Missing `recording_ids` parameter"}
        self.assertDictEqual(resp.json, expected_result)

    @mock.patch('db.data.load_many_high_level')
    def test_get_bulk_hl(self, load_many_high_level):
        # Check that many items are returned, including two offsets of the
        # same mbid

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:3;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2;405a5ff4-7ee2-436b-95c1-90ce8a83b359:3"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        rec_7f = {"recording": "7f27d7a9-27f0-4663-9d20-2c9c40200e6d"}
        rec_40_2 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"}
        rec_40_3 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:3"}

        load_many_high_level.return_value = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "7f27d7a9-27f0-4663-9d20-2c9c40200e6d": {"3": rec_7f},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2, "3": rec_40_3}
        }

        resp = self.client.get('api/v1/high-level?recording_ids=' + params)
        self.assert200(resp)

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "7f27d7a9-27f0-4663-9d20-2c9c40200e6d": {"3": rec_7f},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2, "3": rec_40_3},
            "mbid_mapping": {}
        }
        self.assertDictEqual(resp.json, expected_result)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                      ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 3),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 3)]
        load_many_high_level.assert_called_with(recordings, False)

        # upper-case
        resp = self.client.get('api/v1/high-level?recording_ids=' + params.upper())
        self.assert200(resp)

        expected_result["mbid_mapping"] = {"C5F4909E-1D7B-4F15-A6F6-1AF376BC01C9": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9",
                                           "7F27D7A9-27F0-4663-9D20-2C9C40200E6D": "7f27d7a9-27f0-4663-9d20-2c9c40200e6d",
                                           "405A5FF4-7EE2-436B-95C1-90CE8A83B359": "405a5ff4-7ee2-436b-95c1-90ce8a83b359"}
        self.assertDictEqual(resp.json, expected_result)

    @mock.patch('db.data.load_many_high_level')
    def test_get_bulk_hl_map_classes(self, load_many_high_level):
        # Check that many items are returned, including two offsets of the
        # same mbid

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}

        load_many_high_level.return_value = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
        }

        resp = self.client.get('api/v1/high-level?map_classes=true&recording_ids=' + params)
        self.assert200(resp)

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "mbid_mapping": {}
        }
        self.assertDictEqual(resp.json, expected_result)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0)]
        load_many_high_level.assert_called_with(recordings, True)

        # upper-case
        params = "c5f4909e-1d7b-4f15-a6f6-1AF376BC01C9"
        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "mbid_mapping": {}
        }
        load_many_high_level.return_value = expected_result
        resp = self.client.get('api/v1/high-level?recording_ids=' + params)
        self.assert200(resp)

        # We expect that we get returned whatever we set the mock to, but the argument
        # to load_many_high_level is always lower-case regardless of what we pass in
        self.assertDictEqual(resp.json, expected_result)
        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0)]
        load_many_high_level.assert_called_with(recordings, False)

    @mock.patch('db.data.load_many_high_level')
    def test_get_bulk_hl_absent_mbid(self, load_many_high_level):
        # Check that within a set of mbid parameters, the ones absent
        # from the database are ignored.

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:3;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        rec_40_2 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"}

        load_many_high_level.return_value = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2}
        }

        resp = self.client.get('api/v1/high-level?recording_ids=' + params)
        self.assert200(resp)

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2},
            "mbid_mapping": {}
        }
        self.assertDictEqual(resp.json, expected_result)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                      ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 3),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2)]
        load_many_high_level.assert_called_with(recordings, False)

        # upper-case
        resp = self.client.get('api/v1/high-level?recording_ids=' + params.upper())
        self.assert200(resp)
        # mbid_mapping will include mappings even for items that aren't in the database
        expected_result["mbid_mapping"] = {"C5F4909E-1D7B-4F15-A6F6-1AF376BC01C9": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9",
                                           "7F27D7A9-27F0-4663-9D20-2C9C40200E6D": "7f27d7a9-27f0-4663-9d20-2c9c40200e6d",
                                           "405A5FF4-7EE2-436B-95C1-90CE8A83B359": "405a5ff4-7ee2-436b-95c1-90ce8a83b359"}
        self.assertDictEqual(resp.json, expected_result)

    def test_get_bulk_hl_more_than_25(self):
        # Create many random uuids, because of parameter deduplication
        manyids = [str(uuid.uuid4()) for i in range(26)]
        limit_exceed_url = ";".join(manyids)
        resp = self.client.get('api/v1/high-level?recording_ids=' + limit_exceed_url)
        self.assert400(resp)
        self.assertEqual('More than 25 recordings not allowed per request', resp.json['message'])

    def test_get_bulk_individual_features_no_params(self):
        # Features parameter without a recording_ids parameter is invalid
        features = "lowlevel.average_loudness;rhythm.onset_rate"
        resp = self.client.get('/api/v1/low-level?features=%s' % features)
        self.assertEqual(400, resp.status_code)

        expected_result = {"message": "Missing `recording_ids` parameter"}
        self.assertEqual(expected_result, resp.json)

    @mock.patch('db.data.load_many_individual_features')
    def test_get_bulk_individual_features(self, load_many_individual_features):
        """Check that many items are returned, including two offsets of the
        same MBID.

        If metadata.audio_properties and metadata.version are not requested,
        they are still included.

        Duplicate features and (MBID, offset) combinations are removed."""

        recordings = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;c5f4909e-1d7b-4f15-a6f6-1af376bc01c9:0;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2;405a5ff4-7ee2-436b-95c1-90ce8a83b359:3"
        features = "lowlevel.average_loudness;rhythm.onset_rate;lowlevel.average_loudness"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        rec_40_2 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"}
        rec_40_3 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:3"}

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2, "3": rec_40_3}
        }
        load_many_individual_features.return_value = expected_result

        resp = self.client.get('/api/v1/low-level?recording_ids={}&features={}'.format(recordings, features))
        self.assertEqual(200, resp.status_code)
        self.assertEqual(expected_result, resp.json)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 3)]
        features = [("llj.data->'lowlevel'->'average_loudness'", "lowlevel.average_loudness", None),
                    ("llj.data->'rhythm'->'onset_rate'", "rhythm.onset_rate", None),
                    ("llj.data->'metadata'->'version'", "metadata.version", {}),
                    ("llj.data->'metadata'->'audio_properties'", "metadata.audio_properties", {})]
        load_many_individual_features.assert_called_with(recordings, features)

        # upper-case MBID
        recordings = "c5f4909e-1d7b-4f15-a6f6-1AF376BC01C9"
        features = "lowlevel.average_loudness"
        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5}
        }
        load_many_individual_features.return_value = expected_result
        resp = self.client.get('/api/v1/low-level?recording_ids={}&features={}'.format(recordings, features))
        self.assertEqual(resp.status_code, 200)

        # We expect that we get returned whatever we set the mock to, but the argument
        # to load_many_high_level is always lower-case regardless of what we pass in
        self.assertEqual(resp.json, expected_result)
        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0)]
        features = [("llj.data->'lowlevel'->'average_loudness'", "lowlevel.average_loudness", None),
                    ("llj.data->'metadata'->'version'", "metadata.version", {}),
                    ("llj.data->'metadata'->'audio_properties'", "metadata.audio_properties", {})]
        load_many_individual_features.assert_called_with(recordings, features)

    @mock.patch('db.data.load_many_individual_features')
    def test_get_bulk_individual_features_absent(self, load_many_individual_features):
        # Within MBID parameter, ones absent from the database are ignored.
        # Features that aren't in the list of available features are
        # ignored.
        recordings = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:3;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"
        features = "lowlevel.avg_loudness;rhythm.onset_rate;rhythm.bpm_histogram_first_peak_bpm"

        rec_c5 = {"recording": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        rec_40_2 = {"recording": "405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"}

        expected_result = {
            "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9": {"0": rec_c5},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"2": rec_40_2}
        }
        load_many_individual_features.return_value = expected_result

        resp = self.client.get('/api/v1/low-level?recording_ids={}&features={}'.format(recordings, features))
        self.assertEqual(200, resp.status_code)
        self.assertEqual(expected_result, resp.json)

        recordings = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                      ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 3),
                      ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2)]
        features = [("llj.data->'rhythm'->'onset_rate'", "rhythm.onset_rate", None),
                    ("llj.data->'metadata'->'version'", "metadata.version", {}),
                    ("llj.data->'metadata'->'audio_properties'", "metadata.audio_properties", {})]
        load_many_individual_features.assert_called_with(recordings, features)

    def test_get_bulk_individual_features_more_than_25(self):
        # Create many random uuids, because of parameter deduplication
        manyids = [str(uuid.uuid4()) for i in range(26)]
        limit_exceed_url = ";".join(manyids)
        features = "lowlevel.average_loudness"
        resp = self.client.get('/api/v1/low-level?recording_ids={}&features={}'.format(limit_exceed_url, features))
        self.assertEqual(400, resp.status_code)
        self.assertEqual('More than 25 recordings not allowed per request', resp.json['message'])

    def submit_fake_data(self):
        mbids = ["c5f4909e-1d7b-4f15-a6f6-1af376bc01c9",
                 "7f27d7a9-27f0-4663-9d20-2c9c40200e6d",
                 "405a5ff4-7ee2-436b-95c1-90ce8a83b359",
                 "405a5ff4-7ee2-436b-95c1-90ce8a83b359"]
        # we do not submit the first mbid in order to have a zero count in the
        # response
        for mbid in mbids[1:]:
            self.submit_fake_low_level_data(mbid)
        return mbids

    def test_get_count(self):
        mbids = self.submit_fake_data()
        expected_result = collections.Counter(mbids)
        expected_result[mbids[0]] = 0

        for mbid in mbids:
            resp = self.client.get('api/v1/%s/count' % mbid)
            self.assertEqual(resp.status_code, 200)
            self.assertDictEqual(resp.json,
                                 {"mbid": mbid,
                                  "count": expected_result[mbid]})

        # upper-case
        mbid = "7f27d7a9-27f0-4663-9d20-2c9c40200e6d"
        resp = self.client.get('api/v1/%s/count' % mbid.upper())
        self.assertEqual(resp.status_code, 200)
        self.assertDictEqual(resp.json,
                             {"mbid": mbid,
                              "count": 1})

    def test_get_bulk_count_no_param(self):
        # No parameter in bulk lookup results in an error
        resp = self.client.get('api/v1/count')
        self.assertEqual(resp.status_code, 400)

    def test_get_bulk_count(self):
        mbids = self.submit_fake_data()
        param = ';'.join(mbids)
        resp = self.client.get('api/v1/count?recording_ids=' + param)
        self.assertEqual(resp.status_code, 200)

        expected_result = {
            "7f27d7a9-27f0-4663-9d20-2c9c40200e6d": {"count": 1},
            "405a5ff4-7ee2-436b-95c1-90ce8a83b359": {"count": 2},
            "mbid_mapping": {}
        }
        self.assertDictEqual(resp.json, expected_result)

        # upper-case
        resp = self.client.get('api/v1/count?recording_ids=' + param.upper())
        self.assertEqual(resp.status_code, 200)
        # If the parameters aren't normalised, mbid_mapping will map user's submission -> normalised mbid
        expected_result["mbid_mapping"] = {"7F27D7A9-27F0-4663-9D20-2C9C40200E6D": "7f27d7a9-27f0-4663-9d20-2c9c40200e6d",
                                           "405A5FF4-7EE2-436B-95C1-90CE8A83B359": "405a5ff4-7ee2-436b-95c1-90ce8a83b359",
                                           "C5F4909E-1D7B-4F15-A6F6-1AF376BC01C9": "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9"}
        self.assertDictEqual(resp.json, expected_result)

    def test_get_bulk_count_more_than_25(self):
        # Create many random uuids, because of parameter deduplication
        manyids = [str(uuid.uuid4()) for i in range(26)]
        limit_exceed_url = ";".join(manyids)
        resp = self.client.get('api/v1/count?recording_ids=' + limit_exceed_url)
        self.assertEqual(resp.status_code, 400)
        self.assertEqual('More than 25 recordings not allowed per request',
                         resp.json['message'])


class GetBulkValidationTest(unittest.TestCase):
    # Validation/parse methods don't need to spin up test server
    # or reset database each test

    def test_validate_bulk_params(self):
        # Validate MBIDs, convert offsets to integers, and add offset-0 if not provided
        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:3;405a5ff4-7ee2-436b-95c1-90ce8a83b359:2"
        validated = core._parse_bulk_params(params)

        expected = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                    ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", "7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 3),
                    ("405a5ff4-7ee2-436b-95c1-90ce8a83b359", "405a5ff4-7ee2-436b-95c1-90ce8a83b359", 2)]
        self.assertEqual(expected, validated)

    def test_validate_bulk_params_bad_offset(self):
        # If a parameter is <0 or not an integer, replace it with 0
        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9:-1;7f27d7a9-27f0-4663-9d20-2c9c40200e6d:foo"
        validated = core._parse_bulk_params(params)
        expected = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                    ("7f27d7a9-27f0-4663-9d20-2c9c40200e6d", "7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 0)]
        self.assertEqual(expected, validated)

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9:-1:another"
        with self.assertRaises(webserver.views.api.exceptions.APIBadRequest) as ex:
            core._parse_bulk_params(params)
        self.assertEquals(str(ex.exception), "More than 1 colon (:) in 'c5f4909e-1d7b-4f15-a6f6-1af376bc01c9:-1:another'")

    def test_validate_bulk_params_bad_mbid(self):
        # Return an error if an MBID is invalid
        params = "c5f4909e-1d7b-4f15-a6f6-1af376xxxx:1"
        with self.assertRaises(webserver.views.api.exceptions.APIBadRequest) as ex:
            core._parse_bulk_params(params)
        self.assertEquals(str(ex.exception), "'c5f4909e-1d7b-4f15-a6f6-1af376xxxx' is not a valid UUID")

    def test_validate_bulk_params_deduplicate(self):
        # If the same mbid:offset is provided more than once, only return one

        params = "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9;c5f4909e-1d7b-4f15-a6f6-1af376bc01c9:1;c5f4909e-1d7b-4f15-a6f6-1af376bc01c9:0"
        validated = core._parse_bulk_params(params)

        expected = [("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                    ("c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 1)]
        self.assertEqual(expected, validated)

    def test_validate_bulk_params_normalise(self):
        # If an mbid is not lower-case, or is missing hyphens, reformat it
        params = "C5F4909E-1D7B-4F15-A6F6-1AF376BC01C9;7F27D7A927F046639D202C9C40200E6D"
        validated = core._parse_bulk_params(params)
        expected = [("C5F4909E-1D7B-4F15-A6F6-1AF376BC01C9", "c5f4909e-1d7b-4f15-a6f6-1af376bc01c9", 0),
                    ("7F27D7A927F046639D202C9C40200E6D", "7f27d7a9-27f0-4663-9d20-2c9c40200e6d", 0)]
        self.assertEqual(expected, validated)
