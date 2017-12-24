package com.company.radio.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.company.radio.R;
import com.company.radio.activity.MainActivity;

/**
 * Created by Ashiq on 8/25/2016.
 */
public class RadioNotification {

    private static RadioNotification radioNotification;
    private static Context mContext;

    public static final String ACTION_STOP = "com.codelab.radio.ACTION_STOP";
    public static final String ACTION_PREVIOUS = "com.codelab.radio.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.codelab.radio.ACTION_NEXT";

    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;

    public static RadioNotification getInstance(Context context) {
        if(radioNotification == null) {
            radioNotification = new RadioNotification(context);
        }
        return radioNotification;
    }

    public RadioNotification(Context context) {
        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification(String title, String description) {

        PendingIntent stopIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_STOP), 0);
        PendingIntent previousIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_PREVIOUS), 0);
        PendingIntent nextIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_NEXT), 0);

        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent homeIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.drawable.ic_noti);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(description);
        mBuilder.setTicker(description);
        mBuilder.addAction(R.drawable.ic_previous_gray, null, previousIntent);
        mBuilder.addAction(R.drawable.ic_stop_gray, null, stopIntent);
        mBuilder.addAction(R.drawable.ic_next_gray, null, nextIntent);
        mBuilder.setContentIntent(homeIntent);
        mBuilder.setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    public void updateNotification(String description) {
        mBuilder.setContentText(description);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void clearNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
