package com.caco3.orca.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;


public final class Alarms {

    private Alarms(){
        throw new AssertionError("No instances!");
    }

    /**
     * Schedules alarm for specified interval.
     * Next alarm will be fired in <code>intervalMillis</code> ms
     * @param context any
     * @param receiverClass which will receive intents and start service
     * @param intervalMillis to schedule
     *
     * @throws NullPointerException <code>if (context == null || receiverClass == null)</code>
     * @throws IllegalArgumentException <code>if (intervalMillis < 0) </code>
     */
    public static void scheduleAlarm (Context context,
                                       Class<? extends BroadcastReceiver> receiverClass,
                                       long intervalMillis) {
        Preconditions.checkNotNull(context, "context == null");
        Preconditions.checkNotNull(receiverClass, "receiverClass == null");
        if (intervalMillis < 0) {
            throw new IllegalArgumentException("interval cannot be negative. ("
                    + intervalMillis + " provided)");
        }

        Intent intent = new Intent(context, receiverClass);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + intervalMillis,
                intervalMillis,
                pendingIntent);
    }

    /**
     * Cancels previously schedule alarm
     * @param context any
     * @param receiverClass used to schedule alarm
     *
     * @throws NullPointerException <code>if (context == null || receiverClass == null)</code>
     */
    public static void cancelAlarm(Context context, Class<? extends BroadcastReceiver> receiverClass) {
        Preconditions.checkNotNull(context, "context == null");
        Preconditions.checkNotNull(receiverClass, "receiverClass == null");

        Intent intent = new Intent(context, receiverClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }
}
