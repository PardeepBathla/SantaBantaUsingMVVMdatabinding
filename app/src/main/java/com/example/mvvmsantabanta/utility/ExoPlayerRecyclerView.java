package com.example.mvvmsantabanta.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.exoplayerVideo.CacheDataSourceFactory;
import com.example.mvvmsantabanta.fragments.memes.PlayerViewHolder;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;
import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.mvvmsantabanta.utility.Utils.showLog;


/**
 * Created on : May 24, 2019
 * Author     : AndroidWave
 */
public class ExoPlayerRecyclerView extends RecyclerView {

    public static final String AppName = "Santa Banta";
    private static final String TAG = "ExoPlayerRecyclerView";
    /*  *
     * PlayerViewHolder UI component
     * Watch PlayerViewHolder class*/
    RelativeLayout rl_tag_ic;
    private ImageView mediaCoverImage, volumeControl;
    private RelativeLayout heartLayout;
    private ProgressBar progressBar;
    private ProgressBar pbBuffering;
    private View viewHolderParent;
    private FrameLayout mediaContainer;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;
    /*  *
     * variable declaration*/

    // Media List
    private List<MemesDetailModel> mediaObjects = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;
    // controlling volume state
    private VolumeState volumeState;
    private OnClickListener videoViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleVolume();
        }
    };

    public ExoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ExoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context.getApplicationContext();
        Display display = ((WindowManager) Objects.requireNonNull(
                getContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        videoSurfaceDefaultHeight = point.x;
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = new PlayerView(this.context);
//        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Create the player using ExoPlayerFactory
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        // Disable Player Control
        videoSurfaceView.setUseController(false);
        // Bind the player to the view.
        videoSurfaceView.setPlayer(videoPlayer);
        showLog("setplayer", "setplayer");
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        // Turn on Volume
        setVolumeControl(VolumeState.OFF);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mediaCoverImage != null) {
                        // show the old thumbnail
                        mediaCoverImage.setVisibility(VISIBLE);
                    }


                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic

                    if (!recyclerView.canScrollVertically(1)) {
                        playVideo(true);
                    } else {
                        playVideo(false);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {

                if (viewHolderParent != null && viewHolderParent.equals(view)) {
                    showLog(TAG, "addOnChildAttachStateChangeListener");
                    resetVideoView();
                }
            }
        });

        videoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,
                                        TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        showLog(TAG, "onPlayerStateChanged: Buffering video.");
                        pbBuffering.setVisibility(VISIBLE);
                        pbBuffering.bringToFront();
                        mediaCoverImage.setVisibility(GONE);
                        break;
                    case Player.STATE_ENDED:
                        showLog(TAG, "onPlayerStateChanged: Video ended.");
                        videoPlayer.seekTo(0);
                        pbBuffering.setVisibility(VISIBLE);
                        pbBuffering.bringToFront();
                        break;
                    case Player.STATE_IDLE:
                        showLog(TAG, "STATE_IDLE");
                        break;
                    case Player.STATE_READY:
                        pbBuffering.setVisibility(GONE);
                        showLog(TAG, "onPlayerStateChanged: Ready to play.");
                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }
                        if (!isVideoViewAdded) {
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                showLog(TAG, "onRepeatModeChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                showLog(TAG, "onShuffleModeEnabledChanged");

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                showLog(TAG, "onPlayerError");

            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                showLog(TAG, "onPositionDiscontinuity");

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                showLog(TAG, "onPlaybackParametersChanged");

            }

            @Override
            public void onSeekProcessed() {
                pbBuffering.setVisibility(VISIBLE);
                pbBuffering.bringToFront();
                showLog(TAG, "onSeekProcessed");

            }
        });
    }

    public void playVideo(boolean isEndOfList) {

        int targetPosition;

        if (!isEndOfList) {
            int startPosition = ((LinearLayoutManager) Objects.requireNonNull(getLayoutManager())).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            } else {
                targetPosition = startPosition;
            }
        } else {
            targetPosition = mediaObjects.size() - 1;
        }

        Log.d(TAG, "playVideo: target position: " + targetPosition);

        // video is already playing so return
        if (targetPosition == playPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition;
        if (videoSurfaceView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        int currentPosition =
                targetPosition - ((LinearLayoutManager) Objects.requireNonNull(
                        getLayoutManager())).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if (child == null) {
            return;
        }

        PlayerViewHolder holder = (PlayerViewHolder) child.getTag();
        if (holder == null) {
            playPosition = -1;
            return;
        }
        mediaCoverImage = holder.memesVideoItemBinding.ivMediaCoverImage;
        progressBar = holder.memesVideoItemBinding.progressBar;
        volumeControl = holder.memesVideoItemBinding.ivVolumeControl;

        viewHolderParent = holder.itemView;
        requestManager = holder.requestManager;
        mediaContainer = holder.memesVideoItemBinding.mediaContainer;
        pbBuffering = holder.memesVideoItemBinding.pbBuffering;

        pbBuffering.setVisibility(VISIBLE);

        videoSurfaceView.setPlayer(videoPlayer);



      /* mediaCoverImage.getLayoutParams().height = mediaObjects.get(targetPosition).getImageHeight();
        mediaCoverImage.getLayoutParams().height =  ViewGroup.LayoutParams.MATCH_PARENT;
        mediaCoverImage.requestLayout();

        try {

                if (mediaObjects.get(targetPosition).getImageHeight()!=null || mediaObjects.get(targetPosition).getImageHeight()!=0) {
                    mediaContainer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mediaObjects.get(targetPosition).getImageHeight()));
                    rl_tag_ic.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mediaObjects.get(targetPosition).getImageHeight()));

                }


        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // Turn on Volume
        if (volumeState == VolumeState.ON) {
            setVolumeControl(VolumeState.ON);
        } else {
            setVolumeControl(VolumeState.OFF);
        }


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, AppName));
        String mediaUrl = mediaObjects.get(targetPosition).getImage();
        Log.d("url_media", mediaUrl);
        if (mediaObjects.get(targetPosition).getMemeType().equals("video")) {
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaUrl));
            MediaSource audioSource = new ExtractorMediaSource(Uri.parse(mediaUrl), new CacheDataSourceFactory(context, 100 * 1024 * 1024, 5 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);
            videoPlayer.prepare(audioSource);
            videoPlayer.setPlayWhenReady(true);
            videoPlayer.seekTo(10);
        }

    }

    /*private void animateTagIcon(ImageView ivTagIndicator, boolean b) {

                if (ivTagIndicator != null) {
                    if (b) {
                        ivTagIndicator.setVisibility(VISIBLE);
                        ivTagIndicator.bringToFront();
                        requestManager.load(R.drawable.ic_tag_badge).into(ivTagIndicator);

                        ivTagIndicator.animate().cancel();

                        ivTagIndicator.setAlpha(1f);
                    } else {
                        ivTagIndicator.setAlpha(0f);
                        ivTagIndicator.setImageDrawable(null);
                        ivTagIndicator.requestLayout();
                        ivTagIndicator.animate().cancel();

                    }


                }

    }*/

    /*  *
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight*/

    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) Objects.requireNonNull(
                getLayoutManager())).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        showLog(TAG, "getVisibleVideoSurfaceHeight pos " + playPosition);
        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }

    // Remove the old player
    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            progressBar.setVisibility(GONE);
            mediaCoverImage.setVisibility(VISIBLE);
            viewHolderParent.setOnClickListener(null);


        }
    }

    private void addVideoView() {

        mediaContainer.addView(videoSurfaceView);

        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        mediaCoverImage.setVisibility(GONE);
        volumeControl.setVisibility(VISIBLE);


        if (volumeState == VolumeState.ON) {
            setVolumeControl(VolumeState.ON);
        } else {
            setVolumeControl(VolumeState.OFF);
        }
    }

    private void resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView);
            playPosition = -1;
            volumeControl.setVisibility(GONE);
            videoSurfaceView.setVisibility(INVISIBLE);
            mediaCoverImage.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            if (videoPlayer != null) {
                videoPlayer.stop();
            }
        }
    }

    public void releasePlayer() {
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }

        viewHolderParent = null;
    }

    public void pausePlayer() {
        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(false);
            videoPlayer.getPlaybackState();
        }
    }

    public void startPlayer() {
        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(true);
            videoPlayer.getPlaybackState();
        }
    }

    public void onPausePlayer() {
        if (videoPlayer != null) {
            videoPlayer.stop(true);
        }
    }

    private void toggleVolume() {
        if (videoPlayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                setVolumeControl(VolumeState.ON);
            } else if (volumeState == VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                setVolumeControl(VolumeState.OFF);
            }
        }
    }

    private void setVolumeControl(VolumeState state) {
        volumeState = state;
        if (state == VolumeState.OFF) {
            videoPlayer.setVolume(0f);
            animateVolumeControl();
        } else if (state == VolumeState.ON) {
            videoPlayer.setVolume(1f);
            animateVolumeControl();
        }
    }

    private void animateVolumeControl() {
        if (volumeControl != null) {
            volumeControl.bringToFront();
            if (volumeState == VolumeState.OFF) {
                requestManager.load(R.drawable.ic_volume_off)
                        .into(volumeControl);
            } else if (volumeState == VolumeState.ON) {
                requestManager.load(R.drawable.ic_volume_on)
                        .into(volumeControl);
            }
            volumeControl.animate().cancel();

            volumeControl.setAlpha(1f);

            volumeControl.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(1000);
        }
    }

    public void setMediaObjects(List<MemesDetailModel> mediaObjects) {
        this.mediaObjects = mediaObjects;
        showLog("mediaset", "mediaset");
    }

    //public void onRestartPlayer() {
    //  if (videoPlayer != null) {
    //   playVideo(true);
    //  }
    //}

    /* *
     * Volume ENUM*/


    private enum VolumeState {
        ON, OFF
    }

    public class LoadVideoThumbnail extends AsyncTask<String, Object, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... objectURL) {
            //return ThumbnailUtils.createVideoThumbnail(objectURL[0], Thumbnails.MINI_KIND);
            return ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(objectURL[0], MediaStore.Images.Thumbnails.MINI_KIND), 100, 100);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //img.setImageBitmap(result);
        }

    }

    class extractThumbFromVideo extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String postUrl;

        public extractThumbFromVideo(String postUrl) {
            this.postUrl = postUrl;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            mediaCoverImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //TODO your background code
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(postUrl, new HashMap<>());

            image = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            progressBar.setVisibility(View.GONE);
            if (image != null) {

                videoSurfaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, image.getHeight()));
            }
        }
    }

    class extractThumbFromImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String postUrl;

        public extractThumbFromImage(String postUrl) {
            this.postUrl = postUrl;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            URL url = null;
            try {
                url = new URL(postUrl);

                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            progressBar.setVisibility(View.GONE);
            if (image != null) {
                mediaCoverImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, image.getHeight()));
            }
        }
    }
}
