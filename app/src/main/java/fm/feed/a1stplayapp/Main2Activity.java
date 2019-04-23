package fm.feed.a1stplayapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.Play;
import fm.feed.android.playersdk.models.StationList;

public class Main2Activity extends AppCompatActivity  implements FeedAudioPlayer.StateListener,FeedAudioPlayer.PlayListener {

    FeedAudioPlayer feedPlayer;

    @Override
    protected void onPause() {
        super.onPause();
        if(feedPlayer != null) {
            feedPlayer.removePlayListener(Main2Activity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        feedPlayer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FeedPlayerService.getInstance(new FeedAudioPlayer.AvailabilityListener() {

            @Override
            public void onPlayerAvailable(FeedAudioPlayer feedAudioPlayer) {
                feedPlayer = feedAudioPlayer;
                feedPlayer.setVolume((float) 0.4);
                feedPlayer.prepareToPlay(null);
                stationName.setText(feedAudioPlayer.getActiveStation().getName());
                if(feedAudioPlayer.getActiveStation().containsOption("first_play"))
                {
                    Boolean obj = Boolean.valueOf((String)feedAudioPlayer.getActiveStation().getOption("first_play"));
                    if(obj)
                    {
                        stTextView.setText(getString(R.string.first_workout));
                    }
                    else {
                        stTextView.setText(getString(R.string.second_workout));
                    }
                }
                feedAudioPlayer.addPlayListener(Main2Activity.this);
                feedAudioPlayer.addStateListener(Main2Activity.this);
                if(feedAudioPlayer.getCurrentPlay() != null){
                    onPlayStarted(feedAudioPlayer.getCurrentPlay());
                }
                onStateChanged(feedAudioPlayer.getState());
                playPauseButton.setOnClickListener(view -> {
                    if(videoView.isPlaying()) {
                        videoView.pause();
                        playPauseButton.setText("Play");
                        feedAudioPlayer.pause();
                    }
                    else {
                        videoView.start();
                        playPauseButton.setText("Pause");
                    }

                });
            }

            @Override
            public void onPlayerUnavailable(Exception e) {

            }
        });
    }

    Button playPauseButton;
    TextView stTextView;
    TextView songName;
    TextView stationName;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songName = findViewById(R.id.songName);
        stTextView = findViewById(R.id.tvWorkOutInfoAndList);
        playPauseButton = findViewById(R.id.button);
        stationName = findViewById(R.id.tvStationName);
        videoView = findViewById(R.id.videoView);
        String str = getIntent().getStringExtra("WORKOUT");
        videoView.setVideoPath(str);
        videoView.setOnPreparedListener(mediaPlayer -> {

            playPauseButton.setVisibility(View.VISIBLE);
        });
        videoView.setOnInfoListener((mp, what, extra) -> {

            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                feedPlayer.play();
                return true;
            }
            return false;
        });

        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();


    }
    
    @Override
    public void onStateChanged(FeedAudioPlayer.State state) {

        switch(state) {
            case PLAYING: playPauseButton.setText("Pause"); break;
            case PAUSED:  playPauseButton.setText("Play"); break;
        }
    }

    @Override
    public void onSkipStatusChanged(boolean b) {

    }

    @Override
    public void onProgressUpdate(Play play, float v, float v1) {

    }

    @Override
    public void onPlayStarted(Play play) {

        songName.setText((play.getAudioFile().getTrack().getTitle() +" - " +play.getAudioFile().getArtist().getName()));

    }
}
