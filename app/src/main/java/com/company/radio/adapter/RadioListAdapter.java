package com.company.radio.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.company.radio.R;
import com.company.radio.data.constant.AppConstants;
import com.company.radio.data.constant.RadioCache;
import com.company.radio.listeners.ListItemClickListener;
import com.company.radio.model.RadioModel;

import java.util.ArrayList;

public class RadioListAdapter extends RecyclerView.Adapter<RadioListAdapter.DataObjectHolder> {

    private Context mContext;

    private ArrayList<RadioModel> dataList;

    // handle interface for item listener
    private ListItemClickListener mListItemClickListener;

    private int playingID = -1;

    public RadioListAdapter(Context context, ArrayList<RadioModel> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        RadioModel radioModel = RadioCache.getInstance().getCurrentRadio();
        if(radioModel != null) {
            playingID = radioModel.getRadioId();
        }
    }


    public void setItemClickListener(ListItemClickListener listItemClickListener) {
        mListItemClickListener = listItemClickListener;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvDetails;
        private ImageButton imageButton;

        // handle interface for item listener
        private ListItemClickListener mListItemClickListener;

        public DataObjectHolder(View itemView, ListItemClickListener listItemClickListener) {
            super(itemView);
            this.mListItemClickListener = listItemClickListener;


            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);

            itemView.setOnClickListener(this);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListItemClickListener != null) {
                mListItemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radio, parent, false);
        return new DataObjectHolder(view, mListItemClickListener);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        String title = dataList.get(position).getRadioName();
        String date = dataList.get(position).getCity()+ AppConstants.COMMA + dataList.get(position).getCountry();
        holder.tvTitle.setText(title);
        holder.tvDetails.setText(date);

        if(playingID == dataList.get(position).getRadioId()) {
            holder.imageButton.setImageResource(R.drawable.ic_stop_rounded);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_play_rounded);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
