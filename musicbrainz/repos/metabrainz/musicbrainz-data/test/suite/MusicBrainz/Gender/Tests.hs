{-# LANGUAGE OverloadedStrings #-}
module MusicBrainz.Gender.Tests ( tests ) where

import Test.MusicBrainz
import Test.MusicBrainz.Repository (male)

import qualified Test.MusicBrainz.CommonTests as CommonTests

import MusicBrainz.Versioning

--------------------------------------------------------------------------------
tests :: [Test]
tests = [ testAddGender
        , testResolveReference
        ]

--------------------------------------------------------------------------------
testAddGender :: Test
testAddGender = testCase "Can add Genders" $ do
  CommonTests.testAdd male


--------------------------------------------------------------------------------
testResolveReference :: Test
testResolveReference = testCase "Can resolve Genders" $ do
  CommonTests.testResolveReference (add male) entityRef
