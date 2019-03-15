package fm.feed.a1stplayapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.StationList;

public class MainActivity extends AppCompatActivity implements FeedAudioPlayer.StateListener {

    TextView textView;
    Button playPauseButton;
    Button skipButton;
    Button clearClientIDButton;
    StationList stl;
    StationList stl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FeedAudioPlayer feedAudioPlayer;

        textView = findViewById(R.id.textView);
        playPauseButton =  findViewById(R.id.button);
        skipButton =  findViewById(R.id.button2);
        clearClientIDButton =  findViewById(R.id.button3);

        FeedPlayerService.getInstance(new FeedAudioPlayer.AvailabilityListener() {
            @Override
            public void onPlayerAvailable(final FeedAudioPlayer feedAudioPlayer) {
                stl = feedAudioPlayer.getStationList().getAllStationsWithOption("first_play", "true");
                stl2 = feedAudioPlayer.getStationList().getAllStationsWithOption("first_play", "false");
                feedAudioPlayer.addStateListener(MainActivity.this);
                feedAudioPlayer.addOutOfMusicListener(new FeedAudioPlayer.OutOfMusicListener() {
                    @Override
                    public void onOutOfMusic() {
                        if(feedAudioPlayer.getActiveStation().equals(stl.getFirst()))
                        {
                            feedAudioPlayer.setActiveStation(stl2.getFirst());
                        }
                    }
                });
                textView.setText("Session Available");
                playPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(feedAudioPlayer.getState() == FeedAudioPlayer.State.PLAYING)
                        {
                            feedAudioPlayer.pause();
                        }
                        else if(!stl.isEmpty() && stl.getFirst().hasNewMusic())
                        {
                            feedAudioPlayer.setActiveStation(stl.getFirst());
                            textView.setText(stl.getFirst().getName());
                            feedAudioPlayer.play();
                        }
                        else if(!stl2.isEmpty())
                        {
                            feedAudioPlayer.setActiveStation(stl2.getFirst());
                            textView.setText(stl2.getFirst().getName());
                            feedAudioPlayer.play();
                        }
                    }
                });

                skipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedAudioPlayer.skip();
                    }
                });
                clearClientIDButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView.setText("Resetting client");
                        feedAudioPlayer.createNewClientId(new FeedAudioPlayer.ClientIdListener() {
                            @Override
                            public void onClientId(String s) {
                                feedAudioPlayer.setClientId(s);
                                textView.setText("Client reset! Please kill and then restart the app to test first play again");
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            public void onPlayerUnavailable(Exception e) {

            }
        });
    }

    @Override
    public void onStateChanged(FeedAudioPlayer.State state) {

        switch(state) {
            case PLAYING: playPauseButton.setText("Pause"); break;
            case PAUSED:  playPauseButton.setText("Play"); break;

        }
    }


}
