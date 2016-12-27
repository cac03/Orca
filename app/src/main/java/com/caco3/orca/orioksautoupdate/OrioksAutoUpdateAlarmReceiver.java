package com.caco3.orca.orioksautoupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class OrioksAutoUpdateAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("onReceive()");

        Intent service = new Intent(context, OrioksAutoUpdateService.class);
        context.startService(service);
    }
}
