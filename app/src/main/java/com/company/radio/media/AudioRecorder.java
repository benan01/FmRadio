package com.company.radio.media;

import android.media.MediaRecorder;


/**
 * Created by Ashiq on 8/29/2016.
 */
public class AudioRecorder {

    private MediaRecorder recorder = null;

    public boolean startRecord(String saveTo){
        try {
            recorder = new MediaRecorder();
            recorder.reset();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFile(saveTo);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioEncodingBitRate(16);
            recorder.setAudioSamplingRate(44100);
            recorder.prepare();
            recorder.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording() {
        try {
            if (recorder != null) {
                recorder.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
