package com.company.radio.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.company.radio.model.RadioModel;

import java.util.ArrayList;

/**
 * Created by Ashiq on 7/26/16.
 */
public class RadioDbController {

    private SQLiteDatabase db;

    public RadioDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public int insertData(int radioId, String radioName, String country, String city,
                          String radioType, String streamingUrl) {

        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_RADIO_ID, radioId);
        values.put(DbConstants.COLUMN_RADIO_NAME, radioName);
        values.put(DbConstants.COLUMN_COUNTRY, country);
        values.put(DbConstants.COLUMN_CITY, city);
        values.put(DbConstants.COLUMN_RADIO_TYPE, radioType);
        values.put(DbConstants.COLUMN_STREAMING_URL, streamingUrl);

        // Insert the new row, returning the primary key value of the new row
        return (int) db.insertOrThrow(
                DbConstants.TABLE_NAME_RADIOS,
                DbConstants.COLUMN_NAME_NULLABLE,
                values);
    }

    public ArrayList<RadioModel> getAllData() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        ArrayList<RadioModel> ntyDataArray = new ArrayList<>();
        String[] projection = {
                DbConstants.COLUMN_RADIO_ID,
                DbConstants.COLUMN_RADIO_NAME,
                DbConstants.COLUMN_COUNTRY,
                DbConstants.COLUMN_CITY,
                DbConstants.COLUMN_RADIO_TYPE,
                DbConstants.COLUMN_STREAMING_URL,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants.COLUMN_RADIO_ID + " ASC";

        Cursor c = db.query(
                DbConstants.TABLE_NAME_RADIOS,  // The table name to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            do {
                // get  the  data into array,or class variable
                int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants.COLUMN_RADIO_ID));

                String radioName = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_RADIO_NAME));
                String country = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_COUNTRY));
                String city = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_CITY));
                String radioType = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_RADIO_TYPE));
                String streamingUrl = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_STREAMING_URL));

                // wrap up data list and return
                ntyDataArray.add(new RadioModel(itemId, radioName, country, city, radioType, streamingUrl));
            } while (c.moveToNext());
        }
        c.close();
        return ntyDataArray;
    }


    public void clearTable()   {
        db.delete(DbConstants.TABLE_NAME_RADIOS, null,null);
    }

}
