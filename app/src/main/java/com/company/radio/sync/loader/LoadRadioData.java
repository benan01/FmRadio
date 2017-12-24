package com.company.radio.sync.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.company.radio.listeners.LoaderListener;
import com.company.radio.sync.parser.RadioListParser;
import com.company.radio.data.sqlite.RadioDbController;
import com.company.radio.model.RadioModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ashiq on 8/13/2016.
 */
public class LoadRadioData extends AsyncTask<Void, Void, Object> {

    private Context mContext;
    private LoaderListener loaderListener;

    // db
    private RadioDbController radioDbController;

    public LoadRadioData(Context context) {
        this.mContext = context;
        radioDbController = new RadioDbController(context);
    }

    public void setLoaderListener(LoaderListener loaderListener) {
        this.loaderListener = loaderListener;
    }

    @Override
    protected Object doInBackground(Void... voids) {

        ArrayList<RadioModel> arrayList = radioDbController.getAllData();

        if(arrayList.isEmpty()) {

            String response = syncRadioDataFromAsset();

            arrayList = new RadioListParser().getRadioDataList(response);

            // clear previous data (if any)
            radioDbController.clearTable();

            // insert newly
            for (RadioModel radioModel : arrayList) {
                radioDbController.insertData(radioModel.getRadioId(),
                        radioModel.getRadioName(), radioModel.getCountry(), radioModel.getCity(), radioModel.getRadioType(),
                         radioModel.getStreamingUrl());
            }
            return arrayList;
        } else {
            return arrayList;
        }
    }

    private String syncRadioDataFromAsset() {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = mContext.getAssets().open("radio_data.json");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);

        if(loaderListener != null) {
            loaderListener.onLoadComplete(object);
        }
    }
}
