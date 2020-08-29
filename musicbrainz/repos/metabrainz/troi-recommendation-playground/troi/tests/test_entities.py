import os
import unittest

from troi import Artist, Release, Recording


class TestEntities(unittest.TestCase):

    def test_artist(self):

        a = Artist()
        assert a.mbids == None
        assert a.msid == None
        assert a.name == None
        assert a.artist_credit_id == None
        with self.assertRaises(TypeError):
            a = Artist(mbids="not a list")

        a = Artist("Portishead", ["8fe3f1d4-b6d5-4726-89ad-926e3420b9e3"], "97b01626-65fc-4c32-b30c-c4d7eab1b339")
        assert a.name == "Portishead"
        assert a.mbids == ["8fe3f1d4-b6d5-4726-89ad-926e3420b9e3"]
        assert a.msid == "97b01626-65fc-4c32-b30c-c4d7eab1b339"

        a = Artist("Portishead", ["afe3f1d4-b6d5-4726-89ad-926e3420b9e3", "97b01626-65fc-4c32-b30c-c4d7eab1b339"])
        assert len(a.mbids)
        assert a.mbids[0] == "97b01626-65fc-4c32-b30c-c4d7eab1b339"
        assert a.mbids[1] == "afe3f1d4-b6d5-4726-89ad-926e3420b9e3"

        a = Artist("Portishead", artist_credit_id=65)
        assert a.artist_credit_id == 65

        a = Artist(listenbrainz={ 1 : 2}, musicbrainz={ 3 : 4}, acousticbrainz={ 5 : 6})
        assert a.lb[1] == 2
        assert a.mb[3] == 4
        assert a.ab[5] == 6
        assert a.name == None
        assert a.mbid == None
        assert a.msid == None

    def test_release(self):

        r = Release()
        assert r.mbid == None
        assert r.msid == None
        assert r.name == None

        r = Release("Dummy", "8fe3f1d4-b6d5-4726-89ad-926e3420b9e3", "97b01626-65fc-4c32-b30c-c4d7eab1b339")
        assert r.name == "Dummy"
        assert r.mbid == "8fe3f1d4-b6d5-4726-89ad-926e3420b9e3"
        assert r.msid == "97b01626-65fc-4c32-b30c-c4d7eab1b339"

        r = Release(listenbrainz={ 1 : 2}, musicbrainz={ 3 : 4}, acousticbrainz={ 5 : 6})
        assert r.lb[1] == 2
        assert r.mb[3] == 4
        assert r.ab[5] == 6
        assert r.name == None
        assert r.mbid == None
        assert r.msid == None

    def test_recording(self):

        r = Recording()
        assert r.mbid == None
        assert r.msid == None
        assert r.name == None

        r = Recording("Strangers", "8fe3f1d4-b6d5-4726-89ad-926e3420b9e3", "97b01626-65fc-4c32-b30c-c4d7eab1b339")
        assert r.name == "Strangers"
        assert r.mbid == "8fe3f1d4-b6d5-4726-89ad-926e3420b9e3"
        assert r.msid == "97b01626-65fc-4c32-b30c-c4d7eab1b339"

        r = Recording(listenbrainz={ 1 : 2}, musicbrainz={ 3 : 4}, acousticbrainz={ 5 : 6})
        assert r.lb[1] == 2
        assert r.mb[3] == 4
        assert r.ab[5] == 6
        assert r.name == None
        assert r.mbid == None
        assert r.msid == None
