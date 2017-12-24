package com.company.radio.model;

/**
 * Created by Ashiq on 8/13/2016.
 */
public class RadioModel {

    private int radioId;
    private String radioName;
    private String country;
    private String city;
    private String radioType;
    private String streamingUrl;


    public RadioModel(int radioId, String radioName, String country,
                      String city, String radioType, String streamingUrl) {

        this.radioId = radioId;
        this.radioName = radioName;
        this.country = country;
        this.city = city;
        this.radioType = radioType;
        this.streamingUrl = streamingUrl;
    }


    public String getRadioName() {
        return radioName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreamingUrl() {
        return streamingUrl;
    }

    public int getRadioId() {
        return radioId;
    }

    public String getRadioType() {
        return radioType;
    }
}
