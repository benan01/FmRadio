package com.company.radio.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.radio.R;
import com.company.radio.activity.RadioListActivity;
import com.company.radio.adapter.RadioListAdapter;
import com.company.radio.data.constant.AppConstants;
import com.company.radio.data.constant.RadioCache;
import com.company.radio.listeners.ListItemClickListener;
import com.company.radio.model.RadioModel;
import com.company.radio.utility.SimpleDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Ashiq on 8/3/16.
 */
public class RadioListFragment extends Fragment {

    private Activity mActivity;
    private RecyclerView recyclerView;
    private RadioListAdapter mAdapter;
    private ArrayList<RadioModel> arrayList;
    private int index = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();

        Bundle args = getArguments();
        index = args.getInt(AppConstants.KEY_FRAGMENT_INDEX);

        if(index == 0) {
            arrayList = ((RadioListActivity) mActivity).getFMRadioList();
        } else {
            arrayList = ((RadioListActivity) mActivity).getOnlineRadioList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(mActivity));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RadioListAdapter(mActivity, arrayList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int radioId = arrayList.get(position).getRadioId();
                invokeMainActivity(radioId);
            }
        });


        return rootView;
    }

    private void invokeMainActivity(int radioId) {

        ArrayList<RadioModel> arrayList = RadioCache.getInstance().getRadioList();

        int position = -1;
        for(RadioModel radioModel : arrayList) {
            if(radioModel.getRadioId() == radioId) {
                position = arrayList.indexOf(radioModel);
                break;
            }
        }

        Intent intent = new Intent();
        intent.putExtra(AppConstants.POSITION_BUNDLE_KEY, position);
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();

    }


}

