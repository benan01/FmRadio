package com.company.radio.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.radio.R;
import com.company.radio.activity.MainActivity;
import com.company.radio.data.constant.AppConstants;
import com.company.radio.model.RadioModel;

import java.util.ArrayList;

/**
 * Created by Ashiq on 8/3/16.
 */
public class RadioFragment extends Fragment {

    private Context mContext;
    private ArrayList<RadioModel> arrayList;
    private int index = 0;

    private TextView radioName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();

        Bundle args = getArguments();
        index = args.getInt(AppConstants.KEY_FRAGMENT_INDEX);

        arrayList = ((MainActivity)mContext).getRadioList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_fragment, container, false);

        mContext = getActivity();

        radioName = (TextView) rootView.findViewById(R.id.radioName);

        setListData();

        return rootView;
    }


    private void setListData() {
        if(arrayList != null && !arrayList.isEmpty()) {
            radioName.setText(arrayList.get(index).getRadioName());
        }
    }
}

