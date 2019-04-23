package fm.feed.a1stplayapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;
import fm.feed.android.playersdk.models.Station;
import fm.feed.android.playersdk.models.StationList;

public class MainActivity extends AppCompatActivity {

    final static String RUNNING = "http://demo.feed.fm/Videos/Kristina_2_18_19_2c.mp4";
    final static String CYCLING = "http://demo.feed.fm/Videos/Gregg_03_22_19_2.mp4";

    Button running;
    Button cycling;
    Button clearClientIDButton;
    FeedAudioPlayer feedAudioPlayer;
    TextView resetTv;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        clearClientIDButton =  findViewById(R.id.button7);
        resetTv = findViewById(R.id.resetTv);
        running = findViewById(R.id.button4);

        cycling = findViewById(R.id.button5);
        FeedPlayerService.getInstance(new FeedAudioPlayer.AvailabilityListener() {
            @Override
            public void onPlayerAvailable(final FeedAudioPlayer audioPlayer) {
                feedAudioPlayer = audioPlayer;

                Station cyclingStation = audioPlayer.getStationList().getStationWithName("SinglePlayCycling");
                Station runningStation = audioPlayer.getStationList().getStationWithName("SinglePlayRunning");
                Station recentPop = audioPlayer.getStationList().getStationWithName("Recent Pop");

                running.setOnClickListener(View -> {
                    if(runningStation != null && runningStation.hasNewMusic()) {
                        audioPlayer.setActiveStation(runningStation);
                    }
                    else {
                        audioPlayer.setActiveStation(recentPop);
                    }

                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("WORKOUT", RUNNING);
                    startActivity(intent);

                });

                cycling.setOnClickListener(View -> {
                    if(cyclingStation != null && cyclingStation.hasNewMusic()) {
                        audioPlayer.setActiveStation(cyclingStation);
                    }
                    else {
                        audioPlayer.setActiveStation(recentPop);
                    }

                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("WORKOUT", CYCLING);
                    startActivity(intent);

                });
                clearClientIDButton.setOnClickListener(view -> {
                    resetTv.setText("Resetting Session");
                    audioPlayer.createNewClientId(new FeedAudioPlayer.ClientIdListener() {
                        @Override
                        public void onClientId(String s) {
                            resetTv.setText("Resetting session");
                            audioPlayer.setClientId(s);
                            FeedPlayerService.initialize(getApplicationContext(),"dc46442cd85e0113f970684184c28ebe16f61e5a","df51028d44ff690cc746c1ad5bee4ab4a142f8a1");

                            recreate();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                });
            }

            @Override
            public void onPlayerUnavailable(Exception e) {

            }
        });
    }




}
