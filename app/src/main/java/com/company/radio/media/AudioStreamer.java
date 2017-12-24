package com.company.radio.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.company.radio.listeners.MediaPlayerListener;


/**
 * Created by Ashiq on 7/4/2015.
 */
public class AudioStreamer {

    private String audioLink;

    private MediaPlayer mediaPlayer = null;
    private MediaPlayerListener mediaPlayerListener;

       // VisualizerView
//http://stackoverflow.com/a/8621292

    public AudioStreamer(String audioLink) {
        this.audioLink = audioLink;
        prepareMediaPlayer();
    }

    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        this.mediaPlayerListener = mediaPlayerListener;
    }

    public void prepareMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioLink);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("AudioStreamer", "Prepared");
                    playMedia();

                }
            });


            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.e("AudioStreamer", "Error");
                    return false;
                }
            });


            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMedia() {
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            if(mediaPlayerListener != null) {
                mediaPlayerListener.onRadioPlaying();
            }

        }
    }

    public void pauseResumeMedia() {
        if(mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    public void stopMedia() {
        if(mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
