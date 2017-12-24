package com.company.radio.sync.parser;

import android.util.Log;

import com.company.radio.model.RadioModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashiq on 8/13/2016.
 */
public class RadioListParser {

    public ArrayList<RadioModel> getRadioDataList(String response) {
        ArrayList<RadioModel> arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject radioData = jsonArray.getJSONObject(i);


                if (radioData != null) {

                    int radioId = 0;
                    String radioName = null, country = null, city = null,
                            radioType = null, streamingUrl = null;

                    if (radioData.has(ParserKey.KEY_RADIO_ID)) {
                        radioId = radioData.getInt(ParserKey.KEY_RADIO_ID);
                    }

                    if (radioData.has(ParserKey.KEY_RADIO_NAME)) {
                        radioName = radioData.getString(ParserKey.KEY_RADIO_NAME);
                    }

                    if (radioData.has(ParserKey.KEY_COUNTRY)) {
                        country = radioData.getString(ParserKey.KEY_COUNTRY);
                    }

                    if (radioData.has(ParserKey.KEY_CITY)) {
                        city = radioData.getString(ParserKey.KEY_CITY);
                    }

                    if (radioData.has(ParserKey.KEY_RADIO_TYPE)) {
                        radioType = radioData.getString(ParserKey.KEY_RADIO_TYPE);
                    }

                    if (radioData.has(ParserKey.KEY_STREAMING_URL)) {
                        streamingUrl = radioData.getString(ParserKey.KEY_STREAMING_URL);
                    }

                    Log.e("RadioListParser", "Radio name: "+radioName+", Url: "+streamingUrl);

                    arrayList.add(new RadioModel(radioId, radioName, country, city,
                            radioType, streamingUrl));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


}
