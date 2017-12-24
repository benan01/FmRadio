package com.company.radio.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.company.radio.data.constant.RadioCache;
import com.company.radio.services.MediaService;

/**
 * Created by Ashiq on 8/25/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        context.stopService(new Intent(context, MediaService.class));

        if(action.equalsIgnoreCase(RadioNotification.ACTION_PREVIOUS)){

            RadioCache.getInstance().setPreviousRadio();
            context.startService(new Intent(context, MediaService.class));

            RadioNotification.getInstance(context).updateNotification(RadioCache.getInstance().getCurrentRadio().getRadioName());

        } else if(action.equalsIgnoreCase(RadioNotification.ACTION_STOP)) {

            new RadioNotification(context).clearNotification();
            RadioCache.getInstance().resetPosition();

        } else if(action.equalsIgnoreCase(RadioNotification.ACTION_NEXT)) {

            RadioCache.getInstance().setNextRadio();
            context.startService(new Intent(context, MediaService.class));

            RadioNotification.getInstance(context).updateNotification(RadioCache.getInstance().getCurrentRadio().getRadioName());

        }
    }
}