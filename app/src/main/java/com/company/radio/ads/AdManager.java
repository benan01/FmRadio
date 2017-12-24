package com.company.radio.ads;

import android.content.Context;

import com.company.radio.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Ashiq on 9/3/2016.
 */
public class AdManager {

    private static AdManager adManager;
    private static Context mContext;
    private InterstitialAd mInterstitialAd;

    public static AdManager getAdManager(Context context) {
        if(adManager == null) {
            mContext = context.getApplicationContext();
            adManager = new AdManager();
        }
        return adManager;
    }

    private AdManager() {
        MobileAds.initialize(mContext, mContext.getResources().getString(R.string.app_ad_id));
    }


    public void showBannerAd(AdView adView) {

        // test ad (see log to get your test device id)
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("5D005E9ED1071B3CAA113313B74C0C1E").build();

        // live ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /**
     * first time loading fullscreen ad may take time
     * disable this section if you don't want to show fullscreen ad
     */
    public InterstitialAd loadFullscreenAd() {
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(mContext.getResources().getString(R.string.interstitial_ad_unit_id));

        // test ad (see log to get your test device id)
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("5D005E9ED1071B3CAA113313B74C0C1E").build();

        // live ad
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        return mInterstitialAd;
    }

    public boolean showFullscreenAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            return true;
        }
        return false;
    }

}
