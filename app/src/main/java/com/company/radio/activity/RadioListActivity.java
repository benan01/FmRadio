package com.company.radio.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.company.radio.R;
import com.company.radio.adapter.ListPagerAdapter;
import com.company.radio.ads.AdManager;
import com.company.radio.data.constant.AppConstants;
import com.company.radio.data.constant.RadioCache;
import com.company.radio.data.sqlite.RadioDbController;
import com.company.radio.model.RadioModel;
import com.company.radio.utility.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


/**
 * Created by Ashiq on 8/27/2016.
 */
public class RadioListActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private ViewPager mViewPager;
    private ListPagerAdapter listPagerAdapter;
    private TabLayout tabLayout;

    private ArrayList<RadioModel> arrayList, fmArrayList, onlineArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initView();
        initFunctionality();
        loadData();

    }

    private void initVars() {
        mActivity = RadioListActivity.this;
        mContext = mActivity.getApplicationContext();
        fmArrayList = new ArrayList<>();
        onlineArrayList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_radio_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.all_radio));
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        Utils.noInternetWarning(mViewPager, mContext);
    }

    private void initFunctionality() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // show ad
        AdManager.getAdManager(mContext).showBannerAd((AdView)findViewById(R.id.adView));
    }


    private void loadData() {
       arrayList = RadioCache.getInstance().getRadioList();

        for(RadioModel radioModel : arrayList) {
            if(radioModel.getRadioType().equalsIgnoreCase(AppConstants.RADIO_TYPE_FM)) {
                fmArrayList.add(radioModel);
            } else {
                onlineArrayList.add(radioModel);
            }
        }

        listPagerAdapter = new ListPagerAdapter(getSupportFragmentManager(), 2);
        mViewPager.setAdapter(listPagerAdapter);
        listPagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(mViewPager);
    }

    public ArrayList<RadioModel> getFMRadioList() {
        return fmArrayList;
    }

    public ArrayList<RadioModel> getOnlineRadioList() {
        return onlineArrayList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
