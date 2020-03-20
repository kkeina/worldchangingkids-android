package com.world_changingkids.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.world_changingkids.R;

public class BackgroundSoundService extends Service {

    private static final String TAG = null;
    public static MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.adam_itovsky);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return Service.START_NOT_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }
//
//    public MediaPlayer getPlayer(){
//        return player;
//    }

    //    public void onStop() {
//
//    }
//
//    public void stopBgm(){
//        if(player.isLooping())
//        {
//            player.stop();
//            //player = null;
//        }
//
//    }
//
//
//    public void onPause() {
//
//    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() { }

}
