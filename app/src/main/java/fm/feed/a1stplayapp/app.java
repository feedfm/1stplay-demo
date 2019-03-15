package fm.feed.a1stplayapp;

import android.app.Application;

import fm.feed.android.playersdk.FeedAudioPlayer;
import fm.feed.android.playersdk.FeedPlayerService;

public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FeedAudioPlayer.Builder builder = new FeedAudioPlayer.Builder().setContext(getApplicationContext())
                .setSecret("df51028d44ff690cc746c1ad5bee4ab4a142f8a1")
                .setToken("dc46442cd85e0113f970684184c28ebe16f61e5a");

        FeedPlayerService.initialize(builder);
    }
}
