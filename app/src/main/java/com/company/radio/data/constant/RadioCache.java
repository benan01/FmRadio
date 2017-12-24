package com.company.radio.data.constant;

import com.company.radio.model.RadioModel;

import java.util.ArrayList;

/**
 * Created by Ashiq on 8/27/2016.
 */
public class RadioCache {

    private static RadioCache radioCache;

    private ArrayList<RadioModel> radioList = new ArrayList<>();
    private int currentDataPosition = -1;

    public static RadioCache getInstance() {
        if(radioCache == null) {
            radioCache = new RadioCache();
        }
        return radioCache;
    }

    public void setRadioList(ArrayList<RadioModel> arrayList) {
        radioList.clear();
        radioList.addAll(arrayList);
    }

    public ArrayList<RadioModel> getRadioList() {
        return radioList;
    }

    public void setPosition(int position) {
        currentDataPosition = position;
    }

    public int getPosition() {
        return currentDataPosition;
    }

    public void resetPosition() {
        currentDataPosition = -1;
    }

    public RadioModel getCurrentRadio() {
        if(currentDataPosition != -1) {
            return radioList.get(currentDataPosition);
        }
        return null;
    }

    public boolean isPlaying() {
        return currentDataPosition != -1;
    }

    public void setPreviousRadio() {
        if(isPlaying()) {
            if (currentDataPosition == 0) {
                currentDataPosition = radioList.size() - 1;
            } else {
                currentDataPosition--;
            }
        }
    }

    public void setNextRadio() {
        if (isPlaying()) {
            if (currentDataPosition == (radioList.size() - 1)) {
                currentDataPosition = 0;
            } else {
                currentDataPosition++;
            }
        }
    }


}
