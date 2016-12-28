package com.caco3.orca.settings.settingschanges;

import android.content.Context;

import com.caco3.orca.orioksautoupdate.OrioksAutoUpdateAlarmReceiver;
import com.caco3.orca.util.Alarms;

public class OrioksAutoUpdateDisabledChange extends SettingsChange {

    public OrioksAutoUpdateDisabledChange(Context context) {
        super(context);
    }

    @Override
    public void apply() {
        Alarms.cancelAlarm(getContext(), OrioksAutoUpdateAlarmReceiver.class);
    }
}
