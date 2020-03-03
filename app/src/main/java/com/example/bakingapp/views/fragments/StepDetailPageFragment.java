package com.example.bakingapp.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailPageFragment extends Fragment {

    private static final String STEP_DESCRIPTION = "step_text";
    private static final String STEP_VIDEO_URL = "step_video_url";
    private static final String STEP_THUMBNAIL_URL = "step_thumbnail_url";
    private static final String EXO_CURRENT_POSITION = "exo_current_position";
    private static final String EXO_PLAY_WHEN_READY = "exo_play_when_ready";

    @BindView(R.id.tv_step_details_description) TextView mTvStepDescription;
    @BindView(R.id.exo_player_step_video) PlayerView mExoPlayerView;
    @BindView(R.id.pb_video_loading) ProgressBar mProgressBarVideoLoading;

    private Unbinder mUnbinder;

    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private Long mExoCurrentPosition;
    private Boolean mExoPlayWhenReady;

    public StepDetailPageFragment() {
        // Required empty public constructor
    }

    static StepDetailPageFragment getInstance(String stepDescription) {
        StepDetailPageFragment stepDetailPageFragment = new StepDetailPageFragment();
        attachArgumentToInstance(stepDetailPageFragment, stepDescription, STEP_DESCRIPTION);
        return stepDetailPageFragment;
    }

    static StepDetailPageFragment getInstance(String stepDescription, String videoUrl) {
        StepDetailPageFragment stepDetailPageFragment = new StepDetailPageFragment();
        attachArgumentToInstance(stepDetailPageFragment, stepDescription, STEP_DESCRIPTION);
        attachArgumentToInstance(stepDetailPageFragment, videoUrl, STEP_VIDEO_URL);
        return stepDetailPageFragment;
    }

    static StepDetailPageFragment getInstance(String stepDescription, String videoUrl, String thumbnailUrl) {
        StepDetailPageFragment stepDetailPageFragment = new StepDetailPageFragment();
        attachArgumentToInstance(stepDetailPageFragment, stepDescription, STEP_DESCRIPTION);
        attachArgumentToInstance(stepDetailPageFragment, videoUrl, STEP_VIDEO_URL);
        attachArgumentToInstance(stepDetailPageFragment, thumbnailUrl, STEP_THUMBNAIL_URL);
        return stepDetailPageFragment;
    }

    private static void attachArgumentToInstance(
            StepDetailPageFragment stepDetailPageFragment,
            String argument,
            String argumentKey) {
        Bundle arguments = stepDetailPageFragment.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        arguments.putString(argumentKey, argument);
        stepDetailPageFragment.setArguments(arguments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            mExoCurrentPosition = savedInstanceState.getLong(EXO_CURRENT_POSITION);
            mExoPlayWhenReady = savedInstanceState.getBoolean(EXO_PLAY_WHEN_READY);
        }
        View view = inflater.inflate(R.layout.fragment_step_detail_page, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        getArgumentsValues();
        populateUi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeExoPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoCurrentPosition = mExoPlayer.getCurrentPosition();
            mExoPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoCurrentPosition != null && mExoPlayWhenReady != null) {
            outState.putLong(EXO_CURRENT_POSITION, mExoCurrentPosition);
            outState.putBoolean(EXO_PLAY_WHEN_READY, mExoPlayWhenReady);
        }
    }

    private void initializeExoPlayer() {
        if (mExoPlayer != null) {
            return;
        }
        String url;
        if (mThumbnailUrl != null && !mThumbnailUrl.isEmpty()) {
            url = mThumbnailUrl;
        } else if (mVideoUrl != null && !mVideoUrl.isEmpty()) {
            url = mVideoUrl;
        } else {
            return;
        }
        setProgressBarVisibility(true);
        TrackSelector trackSelector = new DefaultTrackSelector();
        Context context = getContext();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        mExoPlayer.addListener(new ExoPlayerListener());
        mExoPlayer.setPlayWhenReady(mExoPlayWhenReady != null ? mExoPlayWhenReady : true);
        mExoPlayer.prepare(buildMediaSource(url, context));

        mExoPlayerView.setPlayer(mExoPlayer);
    }

    private void setProgressBarVisibility(boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.GONE;
        mProgressBarVideoLoading.setVisibility(visibility);
    }

    private ExtractorMediaSource buildMediaSource(String url, Context context) {
        String exoName = Util.getUserAgent(context, "exo_demo");
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, exoName);

        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseExoPlayer();
    }

    private void releaseExoPlayer() {
        if (mExoPlayer == null) {
            return;
        }
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void populateUi() {
        if (mDescription != null) {
            mTvStepDescription.setText(mDescription);
        }
    }

    private void getArgumentsValues() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }

        if (arguments.containsKey(STEP_DESCRIPTION)) {
            mDescription = arguments.getString(STEP_DESCRIPTION);
        }

        if (arguments.containsKey(STEP_VIDEO_URL)) {
            mVideoUrl = arguments.getString(STEP_VIDEO_URL);
        }

        if (arguments.containsKey(STEP_THUMBNAIL_URL)) {
            mThumbnailUrl = arguments.getString(STEP_THUMBNAIL_URL);
        }
    }

    private void setPlayerVisibility(boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.GONE;
        mExoPlayerView.setVisibility(visibility);
    }

    private class ExoPlayerListener extends DefaultEventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY) {
                setProgressBarVisibility(false);
                setPlayerVisibility(true);
                if (mExoCurrentPosition != null) {
                    mExoPlayer.seekTo(mExoCurrentPosition);
                }
            }
        }
    }
}
