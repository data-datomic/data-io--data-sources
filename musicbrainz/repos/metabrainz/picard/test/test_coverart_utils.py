# -*- coding: utf-8 -*-
#
# Picard, the next-generation MusicBrainz tagger
#
# Copyright (C) 2018 Wieland Hoffmann
# Copyright (C) 2019 Philipp Wolfer
# Copyright (C) 2020 Laurent Monin
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.


import os.path

from test.picardtestcase import PicardTestCase

from picard.coverart.utils import translate_caa_type
from picard.i18n import setup_gettext


class CaaTypeTranslationTest(PicardTestCase):
    def setUp(self):
        super().setUp()
        # we are using temporary locales for tests
        self.tmp_path = self.mktmpdir()
        self.localedir = os.path.join(self.tmp_path, 'locale')
        setup_gettext(self.localedir, "C")

    def test_translating_unknown_types_returns_input(self):
        testtype = "ThisIsAMadeUpCoverArtTypeName"
        self.assertEqual(translate_caa_type(testtype), testtype)
