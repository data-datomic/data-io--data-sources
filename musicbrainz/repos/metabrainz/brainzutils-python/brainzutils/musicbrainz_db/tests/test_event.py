from unittest import TestCase
from mock import MagicMock
from brainzutils.musicbrainz_db import event as mb_event
from brainzutils.musicbrainz_db.test_data import taubertal_festival_2004, event_ra_hall_uk
from brainzutils.musicbrainz_db.unknown_entities import unknown_event


class EventTestCase(TestCase):

    def setUp(self):
        mb_event.mb_session = MagicMock()
        self.mock_db = mb_event.mb_session.return_value.__enter__.return_value
        self.event_query = self.mock_db.query.return_value.filter.return_value.all

    def test_get_event_by_id(self):
        self.event_query.return_value = [taubertal_festival_2004]
        event = mb_event.get_event_by_id('ebe6ce0f-22c0-4fe7-bfd4-7a0397c9fe94')
        self.assertDictEqual(event, {
            'id': 'ebe6ce0f-22c0-4fe7-bfd4-7a0397c9fe94',
            'name': 'Taubertal-Festival 2004, Day 1',
        })

    def test_fetch_multiple_events(self):
        self.event_query.return_value = [taubertal_festival_2004, event_ra_hall_uk]
        events = mb_event.fetch_multiple_events(
            ['ebe6ce0f-22c0-4fe7-bfd4-7a0397c9fe94', '40e6153d-a042-4c95-a0a9-b0a47e3825ce'],
        )
        self.assertEqual(events['ebe6ce0f-22c0-4fe7-bfd4-7a0397c9fe94']['name'],
                         'Taubertal-Festival 2004, Day 1')
        self.assertEqual(events['40e6153d-a042-4c95-a0a9-b0a47e3825ce']['name'],
                         '1996-04-17: Royal Albert Hall, London, England, UK')

    def test_fetch_multiple_events_empty(self):
        self.event_query.return_value = []
        events = mb_event.fetch_multiple_events([
            'ebe6ce0f-22c0-4fe7-bfd4-7a0397c9fe94',
            '40e6153d-a042-4c95-a0a9-b0a47e3825ce'
        ],
        includes=['artist-rels', 'place-rels', 'series-rels', 'url-rels', 'release-group-rels'],
        unknown_entities_for_missing=True)
        self.assertEqual(events['ebe6ce0f-22c0-4fe7-bfd4-7a0397c9fe94']['name'],
                         unknown_event.name)
        self.assertEqual(events['40e6153d-a042-4c95-a0a9-b0a47e3825ce']['name'],
                         unknown_event.name)
