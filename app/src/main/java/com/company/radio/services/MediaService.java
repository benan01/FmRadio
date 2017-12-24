package com.company.radio.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.company.radio.data.constant.AppConstants;
import com.company.radio.data.constant.RadioCache;
import com.company.radio.listeners.MediaPlayerListener;
import com.company.radio.media.AudioStreamer;
import com.company.radio.model.RadioModel;
import com.company.radio.notification.NotificationReceiver;
import com.company.radio.notification.RadioNotification;

/**
 * Created by Ashiq on 8/25/2016.
 */
public class MediaService extends Service implements MediaPlayerListener {

    private NotificationReceiver broadcastReceiver;
    private AudioStreamer audioStreamer;

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
        startStream();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        stopStream();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startStream() {
        stopStream();
        if(RadioCache.getInstance().isPlaying()) {

            RadioModel radioModel = RadioCache.getInstance().getCurrentRadio();
            Log.e("MediaService", "Currently playing: "+radioModel.getRadioName());

            String radioUrl = radioModel.getStreamingUrl();
            audioStreamer = new AudioStreamer(radioUrl);
            audioStreamer.setMediaPlayerListener(this);
        }
    }

    private void stopStream() {
        if (audioStreamer != null) {
            audioStreamer.stopMedia();
            //audioStreamer = null;
            //RadioCache.getInstance().resetPosition();
        }
    }

    private void registerReceiver() {
        broadcastReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addAction(RadioNotification.ACTION_NEXT);
        intentFilter.addAction(RadioNotification.ACTION_PREVIOUS);
        intentFilter.addAction(RadioNotification.ACTION_STOP);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void sendRadioStatusBroadcast() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.RADIO_STATUS);
        LocalBroadcastManager.getInstance(MediaService.this).sendBroadcast(intent);
    }


    @Override
    public void onRadioPlaying() {
        sendRadioStatusBroadcast();
    }
}
