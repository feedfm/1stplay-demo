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

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.Play;
import fm.feed.android.playersdk.models.StationList;

public class Main2Activity extends AppCompatActivity  implements FeedAudioPlayer.PlayListener, Player.EventListener {

    FeedAudioPlayer feedPlayer;
    PlayerView playerView;
    SimpleExoPlayer player;

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
        player.stop();
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
                        if(getIntent().getStringExtra("WORKOUT_TYPE").contains("CYCLING"))
                        {
                            stTextView.setText(getString(R.string.first_workout_cycling));
                        }
                        else {
                            stTextView.setText(getString(R.string.first_workout_running));
                        }
                    }
                    else {
                        stTextView.setText(getString(R.string.second_workout));
                    }
                }
                feedAudioPlayer.addPlayListener(Main2Activity.this);
                if(feedAudioPlayer.getCurrentPlay() != null){
                    onPlayStarted(feedAudioPlayer.getCurrentPlay());
                }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songName = findViewById(R.id.songName);
        stTextView = findViewById(R.id.tvWorkOutInfoAndList);
        playPauseButton = findViewById(R.id.button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedPlayer.skip();
            }
        });
        stationName = findViewById(R.id.tvStationName);
        playerView = findViewById(R.id.video_view);
        initializePlayer();

    }

    private void initializePlayer() {

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        DefaultLoadControl loadControl = new DefaultLoadControl();
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(Main2Activity.this);
        player = ExoPlayerFactory.newSimpleInstance(Main2Activity.this, renderersFactory, trackSelector, loadControl);
         String str = getIntent().getStringExtra("WORKOUT");
        play(str);
    }

    private  void play(String url) {
        String userAgent = Util.getUserAgent(Main2Activity.this, Main2Activity.this.getString(R.string.app_name));

        /*ExtractorMediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(Main2Activity.this, userAgent))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(url));
        *///3


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Main2Activity.this,userAgent);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));

        player.prepare(mediaSource);
        player.addListener(Main2Activity.this);
        playerView.setPlayer(player);
        player.setPlayWhenReady(false);
    }



    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                feedPlayer.pause();
                break;
            case Player.STATE_ENDED:
                feedPlayer.pause();
                break;
            case Player.STATE_IDLE:
                feedPlayer.pause();
                break;
            case Player.STATE_READY:
                if(player.getPlayWhenReady()) {
                    feedPlayer.play();
                }
                else {
                    feedPlayer.pause();
                }
                break;
            default:
                break;
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
