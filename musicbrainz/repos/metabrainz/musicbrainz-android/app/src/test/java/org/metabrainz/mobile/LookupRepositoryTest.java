package org.metabrainz.mobile;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.util.Resource;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.metabrainz.mobile.AssertionUtils.checkArtistAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkLabelAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkRecordingAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseGroupAssertions;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtist;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtistMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabel;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabelMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecording;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecordingMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestRelease;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroup;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroupMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseMBID;
import static org.metabrainz.mobile.EntityTestUtils.loadResourceAsString;
import static org.metabrainz.mobile.LiveDataTestUtil.getOrAwaitValue;
import static org.metabrainz.mobile.RetrofitUtils.createTestService;

public class LookupRepositoryTest {

    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();
    MockWebServer webServer;
    LookupRepository repository;

    @Before
    public void setup() {
        try {
            webServer = new MockWebServer();
            webServer.setDispatcher(new Dispatcher() {
                @NotNull
                @Override
                public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                    String endpoint = recordedRequest.getPath()
                            .substring(1, recordedRequest.getPath().indexOf('/', 1));
                    String file = endpoint + "_lookup.json";
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(loadResourceAsString(file));
                }
            });
            webServer.start();
            LookupService service = createTestService(LookupService.class, webServer.url("/"));
            repository = new LookupRepository(service);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testArtistLookup() {
        Artist testArtist = getTestArtist();
        LiveData<Resource<String>> testArtistData = repository.fetchData(MBEntityType.ARTIST.name,
                getTestArtistMBID(), Constants.LOOKUP_ARTIST_PARAMS);
        try {
            Resource<String> resource = getOrAwaitValue(testArtistData);
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            String response = resource.getData();
            Artist artist = new Gson().fromJson(response, Artist.class);
            checkArtistAssertions(testArtist, artist);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReleaseLookup() {
        Release testRelease = getTestRelease();
        LiveData<Resource<String>> testReleaseData = repository.fetchData(MBEntityType.RELEASE.name,
                getTestReleaseMBID(), Constants.LOOKUP_RELEASE_PARAMS);
        try {
            Resource<String> resource = getOrAwaitValue(testReleaseData);
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            String response = resource.getData();
            Release release = new Gson().fromJson(response, Release.class);
            checkReleaseAssertions(testRelease, release);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReleaseGroupLookup() {
        ReleaseGroup testReleaseGroup = getTestReleaseGroup();
        LiveData<Resource<String>> testReleaseGroupData = repository.fetchData(MBEntityType.RELEASE_GROUP.name,
                getTestReleaseGroupMBID(), Constants.LOOKUP_RELEASE_GROUP_PARAMS);
        try {
            Resource<String> resource = getOrAwaitValue(testReleaseGroupData);
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            String response = resource.getData();
            ReleaseGroup releaseGroup = new Gson().fromJson(response, ReleaseGroup.class);
            checkReleaseGroupAssertions(testReleaseGroup, releaseGroup);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLabelLookup() {
        Label testLabel = getTestLabel();
        LiveData<Resource<String>> testLabelData = repository.fetchData(MBEntityType.LABEL.name,
                getTestLabelMBID(), Constants.LOOKUP_LABEL_PARAMS);
        try {
            Resource<String> resource = getOrAwaitValue(testLabelData);
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            String response = resource.getData();
            Label label = new Gson().fromJson(response, Label.class);
            checkLabelAssertions(testLabel, label);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRecordingLookup() {
        Recording testRecording = getTestRecording();
        LiveData<Resource<String>> testRecordingData = repository.fetchData(MBEntityType.RECORDING.name,
                getTestRecordingMBID(), Constants.LOOKUP_RECORDING_PARAMS);
        try {
            Resource<String> resource = getOrAwaitValue(testRecordingData);
            assertEquals(Resource.Status.SUCCESS, resource.getStatus());
            String response = resource.getData();
            Recording recording = new Gson().fromJson(response, Recording.class);
            checkRecordingAssertions(testRecording, recording);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void teardown() {
        try {
            webServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}