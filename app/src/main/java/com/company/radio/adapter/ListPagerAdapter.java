package com.company.radio.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.company.radio.data.constant.AppConstants;
import com.company.radio.fragment.RadioListFragment;

/**
 * Created by Ashiq on 8/3/16.
 */
public class ListPagerAdapter extends FragmentStatePagerAdapter {

    private int totalPager;

    public ListPagerAdapter(FragmentManager fm, int totalPager) {
        super(fm);
        this.totalPager = totalPager;
    }

    @Override
    public Fragment getItem(int i) {

        RadioListFragment radioListFragment = new RadioListFragment();
        Bundle args= new Bundle();
        args.putInt(AppConstants.KEY_FRAGMENT_INDEX, i);
        radioListFragment.setArguments(args);

        return radioListFragment;

    }

    @Override
    public int getCount() {
        return totalPager;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return AppConstants.RADIO_TYPE_FM;
        } else {
            return AppConstants.RADIO_TYPE_ONLINE;
        }
    }



}
