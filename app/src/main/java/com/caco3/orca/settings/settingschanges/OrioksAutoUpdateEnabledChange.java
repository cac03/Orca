package com.caco3.orca.settings.settingschanges;

import android.content.Context;

import com.caco3.orca.orioksautoupdate.OrioksAutoUpdateAlarmReceiver;
import com.caco3.orca.util.Alarms;

public class OrioksAutoUpdateEnabledChange extends SettingsChange {
    private final long interval;

    public OrioksAutoUpdateEnabledChange(Context context, long interval) {
        super(context);
        if (interval < 0) {
            throw new IllegalArgumentException("interval < 0. interval = " + interval);
        }
        this.interval = interval;
    }

    @Override
    public void apply() {
        Alarms.scheduleAlarm(getContext(), OrioksAutoUpdateAlarmReceiver.class, interval);
    }
}
