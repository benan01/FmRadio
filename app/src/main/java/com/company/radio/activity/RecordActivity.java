package com.company.radio.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.company.radio.R;
import com.company.radio.ads.AdManager;
import com.company.radio.data.constant.AppConstants;
import com.company.radio.data.constant.RadioCache;
import com.company.radio.media.AudioRecorder;
import com.company.radio.model.RadioModel;
import com.company.radio.utility.InputDialog;
import com.company.radio.utility.PermissionUtils;
import com.company.radio.utility.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ashiq on 8/29/2016.
 */
public class RecordActivity  extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RadioModel currentModel;
    private boolean isRecordingRunning = false;
    private AudioRecorder audioRecorder;
    private ShimmerFrameLayout container;

    private FloatingActionButton stopButton;
    private TextView radioName, countUpTimer;

    private Timer timer;
    private int duration = 0;

    private String audioName, saveTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initView();
        initFunctionality();
        initListeners();

    }

    private void initVars() {
        mActivity = RecordActivity.this;
        mContext = mActivity.getApplicationContext();
    }

    private void initView() {
        setContentView(R.layout.activity_record);

        stopButton = (FloatingActionButton) findViewById(R.id.stopButton);
        container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        radioName = (TextView) findViewById(R.id.radioName);
        countUpTimer = (TextView) findViewById(R.id.countUpTimer);
    }

    private void initFunctionality() {
        currentModel = RadioCache.getInstance().getCurrentRadio();
        radioName.setText(currentModel.getRadioName());

        audioName = currentModel.getRadioName() + AppConstants.SPACE + Utils.getFormattedDateTime();
        saveTo = Utils.generateSdCardUrl(AppConstants.RECORD_DIRECTORY, audioName + AppConstants.RECORD_SUFFIX);

        recordRadio();

        // show ad
        AdManager.getAdManager(mContext).showBannerAd((AdView)findViewById(R.id.adView));
    }

    private void initListeners() {
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputDialog.showInputDialog(mActivity, audioName, getString(R.string.save_recording),
                        getString(R.string.save), getString(R.string.discard), new InputDialog.InputDialogActionListener() {
                            @Override
                            public void onSave(String fileName) {
                                renameAndExit(fileName);
                            }

                            @Override
                            public void onDiscard() {
                                Utils.deleteFile(saveTo);
                                finish();
                            }
                        });

                recordRadio();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_RECORD_AUDIO: {
                if(PermissionUtils.isPermissionResultGranted(grantResults)) {
                    recordRadio();
                } else {
                    Utils.showSnackbar(stopButton, getString(R.string.permission_denied));
                }
            }
        }

    }

    private void recordRadio() {
        if(isRecordingRunning) {
            closeRecording();
            container.stopShimmerAnimation();
            isRecordingRunning = false;
        } else {
            if (PermissionUtils.isPermissionGranted(mActivity, PermissionUtils.RECORD_PERMISSIONS, PermissionUtils.REQUEST_RECORD_AUDIO)) {
                audioRecorder = new AudioRecorder();
                boolean isRecording = audioRecorder.startRecord(saveTo);

                if(!isRecording) {
                    Toast.makeText(mContext, getString(R.string.error_record), Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    isRecordingRunning = true;
                    container.startShimmerAnimation();
                    showCountUpTimer();
                }
            }

        }

    }

    private void closeRecording() {
        if(audioRecorder != null) {
            audioRecorder.stopRecording();
            audioRecorder = null;
        }
    }


    private void showCountUpTimer() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        } else {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            duration++;
                            countUpTimer.setText(Utils.formatDuration(duration));
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    private void renameAndExit(String newName) {
        if(!newName.equals(audioName)) {
            String newPath = Utils.generateSdCardUrl(AppConstants.RECORD_DIRECTORY, newName + AppConstants.RECORD_SUFFIX);
            Utils.renameFile(saveTo, newPath);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRecording();
    }
}