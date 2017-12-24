package com.company.radio.data.sqlite;

/**
 * Created by Ashiq on 7/26/16.
 */
public class DbConstants {

    // commons
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String COLUMN_NAME_NULLABLE = null;

    public static final String TABLE_NAME_RADIOS = "radios";

    public static final String COLUMN_RADIO_ID = "radio_id";
    public static final String COLUMN_RADIO_NAME = "radio_name";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_RADIO_TYPE = "radio_type";
    public static final String COLUMN_STREAMING_URL = "streaming_url";


    public static final String SQL_CREATE_RADIO_ENTRIES =
            "CREATE TABLE " + TABLE_NAME_RADIOS + " (" +
                    COLUMN_RADIO_ID + INT_TYPE+ " PRIMARY KEY" + COMMA_SEP +
                    COLUMN_RADIO_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_COUNTRY + TEXT_TYPE + COMMA_SEP +
                    COLUMN_RADIO_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CITY + TEXT_TYPE + COMMA_SEP +
                    COLUMN_STREAMING_URL + TEXT_TYPE +
                    " )";



    public static final String SQL_DELETE_RADIO_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME_RADIOS;

}
