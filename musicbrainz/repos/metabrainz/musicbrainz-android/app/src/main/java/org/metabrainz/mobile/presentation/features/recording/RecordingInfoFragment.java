package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.databinding.FragmentRecordingInfoBinding;
import org.metabrainz.mobile.util.Resource;

public class RecordingInfoFragment extends Fragment {

    private FragmentRecordingInfoBinding binding;
    private RecordingViewModel recordingViewModel;

    public static RecordingInfoFragment newInstance() {
        return new RecordingInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecordingInfoBinding.inflate(inflater, container, false);
        recordingViewModel = new ViewModelProvider(requireActivity()).get(RecordingViewModel.class);
        recordingViewModel.getData().observe(getViewLifecycleOwner(), this::setRecordingInfo);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecordingInfo(Resource<Recording> resource) {
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            Recording recording = resource.getData();
            String duration, artist;
            binding.recordingTitle.setText(recording.getTitle());
            duration = recording.getDuration();
            artist = EntityUtils.getDisplayArtist(recording.getArtistCredits());
            if (duration != null) binding.recordingDuration.setText(duration);
            binding.recordingArtist.setText(artist);
        }
    }
}
