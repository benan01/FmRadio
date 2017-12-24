package com.company.radio.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.company.radio.data.constant.AppConstants;
import com.company.radio.fragment.RadioFragment;
import com.company.radio.model.RadioModel;

import java.util.ArrayList;

/**
 * Created by Ashiq on 8/3/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<RadioModel> mFragmentItems;


    public ViewPagerAdapter(FragmentManager fm, ArrayList<RadioModel> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
    }

    @Override
    public Fragment getItem(int i) {

        RadioFragment radioFragment = new RadioFragment();
        Bundle args= new Bundle();
        args.putInt(AppConstants.KEY_FRAGMENT_INDEX, i);
        radioFragment.setArguments(args);

        return radioFragment;

    }

    @Override
    public int getCount() {
        return mFragmentItems.size();
    }

    /*@Override
    public CharSequence getPageTitle(int position) {
        return mFragmentItems.get(position);
    }*/



}
