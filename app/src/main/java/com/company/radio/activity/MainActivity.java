package com.company.radio.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.radio.R;
import com.company.radio.adapter.ViewPagerAdapter;
import com.company.radio.ads.AdManager;
import com.company.radio.listeners.LoaderListener;
import com.company.radio.sync.loader.LoadRadioData;
import com.company.radio.data.constant.AppConstants;
import com.company.radio.data.constant.RadioCache;
import com.company.radio.model.RadioModel;
import com.company.radio.notification.RadioNotification;
import com.company.radio.services.MediaService;
import com.company.radio.utility.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<RadioModel> arrayList;
    private RadioModel currentModel;

    private LinearLayout loadingView;
    private FloatingActionButton playButton;
    private TextView playingNow;
    private ImageView playStatus;
    private ImageButton twitter,mail,instagram;
    private ImageView leftArrowButton, rightArrowButton;
    private ShimmerFrameLayout container;

    private int currentPlayingRadioId = -1;
    private String currentPlayingRadioName;

    private RadioNotification radioNotification;
    private RadioStatusReceiver radioStatusReceiver;

    private MenuItem allRadioMenu, recordMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initView();
        initFunctionality();
        initListeners();
        loadData();
        ImageButton twitterr = (ImageButton)findViewById(R.id.imageButton2);
        ImageButton instagramm = (ImageButton)findViewById(R.id.imageButton3);
        ImageButton mail = (ImageButton)findViewById(R.id.imageButton4);
        twitterr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("https://twitter.com/medine_fm/"));
                startActivity(myWebLink);
            }
        });
        instagramm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("https://www.instagram.com/_medinefm.net_/"));
                startActivity(myWebLink);
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "radyomevlana@hotmail.com", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initVars() {
        mActivity = MainActivity.this;
        mContext = mActivity.getApplicationContext();
        arrayList = new ArrayList<>();
        radioNotification = RadioNotification.getInstance(mContext);
    }

    private void initView() {

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        loadingView = (LinearLayout) findViewById(R.id.loading_view);

        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        playingNow = (TextView) findViewById(R.id.playingNow);
        playStatus = (ImageView) findViewById(R.id.playStatus);
        leftArrowButton = (ImageButton) findViewById(R.id.leftArrowButton);
        rightArrowButton = (ImageButton) findViewById(R.id.rightArrowButton);
        container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

    }

    private void initFunctionality() {
        registerReceiver();
        Utils.noInternetWarning(playButton, mContext);

        // show ad
        AdManager.getAdManager(mContext).showBannerAd((AdView) findViewById(R.id.adView));

        // show fullscreen ad (disable this section if you don't want fullscreen ad)
        InterstitialAd mInterstitialAd = AdManager.getAdManager(mContext).loadFullscreenAd();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                invokeRadioListActivity();
            }
        });

    }

    private void initListeners() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentModel = arrayList.get(position);
                manageArrowButtons(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playRadio();
            }
        });

        leftArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerSwipe(false);
            }
        });

        rightArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerSwipe(true);
            }
        });
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), arrayList);
        mViewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        if (RadioCache.getInstance().isPlaying()) {
            mViewPager.setCurrentItem(RadioCache.getInstance().getPosition(), true);
        }
    }

    private void manageArrowButtons(int position) {
        if (position == 0) {
            rightArrowButton.setVisibility(View.VISIBLE);
            leftArrowButton.setVisibility(View.GONE);
        } else if (position == (arrayList.size() - 1)) {
            rightArrowButton.setVisibility(View.GONE);
            leftArrowButton.setVisibility(View.VISIBLE);
        } else {
            rightArrowButton.setVisibility(View.VISIBLE);
            leftArrowButton.setVisibility(View.VISIBLE);
        }

        if (arrayList.get(position).getRadioId() == currentPlayingRadioId) {
            playButton.setImageResource(R.drawable.ic_stop);
        } else {
            playButton.setImageResource(R.drawable.ic_play);
        }
    }

    private void pagerSwipe(boolean right) {
        if (right) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        }
    }

    private void loadData() {
        loadingView.setVisibility(View.VISIBLE);

        LoadRadioData loadRadioData = new LoadRadioData(mContext);
        loadRadioData.setLoaderListener(new LoaderListener() {
            @Override
            public void onLoadComplete(Object data) {
                arrayList = (ArrayList<RadioModel>) data;
                if (arrayList != null && !arrayList.isEmpty()) {
                    currentModel = arrayList.get(0);
                    setupViewPager();

                    rightArrowButton.setVisibility(View.VISIBLE);
                    playButton.show();
                    loadingView.setVisibility(View.GONE);

                    if (allRadioMenu != null) {
                        allRadioMenu.setVisible(true);
                        //invalidateOptionsMenu();
                    }
                }

                // cache data to use from service
                RadioCache.getInstance().setRadioList(arrayList);
            }
        });
        loadRadioData.execute();
    }

    private void playRadio() {

        // stop playing radio first
        stopService(new Intent(mContext, MediaService.class));

        if (currentModel != null && currentModel.getRadioId() != currentPlayingRadioId) {
            playButton.setImageResource(R.drawable.ic_stop);
            playStatus.setImageResource(R.drawable.ic_radio);
            container.startShimmerAnimation();
            playingNow.setText(getString(R.string.now_playing) + AppConstants.SPACE + currentModel.getRadioName());

            currentPlayingRadioId = currentModel.getRadioId();
            currentPlayingRadioName = currentModel.getRadioName();
            RadioCache.getInstance().setPosition(arrayList.indexOf(currentModel));
            startService(new Intent(mContext, MediaService.class));

            if (recordMenu != null) {
                recordMenu.setEnabled(true);
            }

        } else {
            playButton.setImageResource(R.drawable.ic_play);
            playStatus.setImageResource(R.drawable.ic_radio_gray);
            container.stopShimmerAnimation();

            currentPlayingRadioId = -1;
            currentPlayingRadioName = null;
            RadioCache.getInstance().resetPosition();

            if (recordMenu != null) {
                recordMenu.setEnabled(false);
            }

        }
    }

    private void restoreRadioState() {
        if (RadioCache.getInstance().isPlaying()) {
            RadioModel radioModel = RadioCache.getInstance().getCurrentRadio();
            playButton.setImageResource(R.drawable.ic_stop);
            playStatus.setImageResource(R.drawable.ic_radio);
            playingNow.setText(getString(R.string.now_playing) + AppConstants.SPACE + radioModel.getRadioName());

            currentPlayingRadioId = radioModel.getRadioId();
            currentPlayingRadioName = radioModel.getRadioName();

            mViewPager.setCurrentItem(RadioCache.getInstance().getPosition(), true);

            radioNotification.clearNotification();
        } else {
            playButton.setImageResource(R.drawable.ic_play);
            playStatus.setImageResource(R.drawable.ic_radio_gray);
            playingNow.setText(getString(R.string.not_playing));

            currentPlayingRadioId = -1;
            currentPlayingRadioName = null;

            if (recordMenu != null) {
                recordMenu.setEnabled(false);
            }
        }
    }


    public ArrayList<RadioModel> getRadioList() {
        return arrayList;
    }

    private void registerReceiver() {
        radioStatusReceiver = new RadioStatusReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.RADIO_STATUS);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(radioStatusReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (radioStatusReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(radioStatusReceiver);
        }
    }

    private class RadioStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (container != null) {
                container.stopShimmerAnimation();
            }
        }
    }

    private void invokeRadioListActivity() {
        Intent intent = new Intent(mActivity, RadioListActivity.class);
        startActivityForResult(intent, AppConstants.RADIO_REQ_CODE);
    }

    private void invokeRecordActivity() {
        Intent intent = new Intent(mActivity, RecordActivity.class);
        startActivity(intent);
    }

    private void shareRadio() {
        String text = getString(R.string.share_text) + AppConstants.SPACE + getString(R.string.app_name)
                + AppConstants.SPACE + Utils.getPlayStoreLink(mContext);
        if(RadioCache.getInstance().isPlaying()) {
            text = getString(R.string.share_text1) + AppConstants.SPACE + currentPlayingRadioName +
                    AppConstants.SPACE + getString(R.string.share_text2) + AppConstants.COMMA + getString(R.string.app_name) +
                    AppConstants.COMMA + getString(R.string.share_text3) + AppConstants.SPACE + Utils.getPlayStoreLink(mContext);
        }
        Utils.shareApp(mActivity, text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.RADIO_REQ_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                int position = extras.getInt(AppConstants.POSITION_BUNDLE_KEY);
                currentModel = arrayList.get(position);
                playRadio();
                manageArrowButtons(position);
                mViewPager.setCurrentItem(position, true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //allRadioMenu = menu.findItem(R.id.action_list);
        recordMenu = menu.findItem(R.id.action_record);
       /* if (arrayList != null && arrayList.isEmpty()) {
            allRadioMenu.setVisible(false);
        } else {
            allRadioMenu.setVisible(true);
        }
*/
        if (RadioCache.getInstance().isPlaying()) {
            recordMenu.setEnabled(true);
        } else {
            recordMenu.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
      /*  if (id == R.id.action_list) {

            // use invokeRadioListActivity(); instead if you don't want to show fullscreen ad
            if (!AdManager.getAdManager(mContext).showFullscreenAd()) {
                invokeRadioListActivity();
            }

            return true;
        } else*/ if (id == R.id.action_share) {
            shareRadio();
        } else if (id == R.id.action_record) {
            invokeRecordActivity();
        } else if (id == R.id.action_rate) {
            Utils.rateThisApp(mActivity);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreRadioState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentPlayingRadioName != null) {
            radioNotification.showNotification(getString(R.string.app_name), currentPlayingRadioName);
        }
    }
}
