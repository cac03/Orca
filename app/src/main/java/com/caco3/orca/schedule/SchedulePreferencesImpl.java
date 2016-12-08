package com.caco3.orca.schedule;

import android.content.Context;

import javax.inject.Inject;

/**
 * Concrete {@link SchedulePreferences} implementation where {@link android.content.SharedPreferences}
 * used to store data
 */
/*package*/final class SchedulePreferencesImpl implements SchedulePreferences {
    private static final String PREFERENCES_FILENAME = "schedule";

    private static final String KEY_GROUP_NAME_TO_SHOW_SCHEDULE_FOR
            = "group_name";
    private Context context;

    @Inject
    /*package*/ SchedulePreferencesImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getGroupToShowScheduleFor() {
        return context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
                .getString(KEY_GROUP_NAME_TO_SHOW_SCHEDULE_FOR, null);
    }

    @Override
    public void setGroupToShowScheduleFor(String groupNameToShowScheduleFor) {
        context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_GROUP_NAME_TO_SHOW_SCHEDULE_FOR, groupNameToShowScheduleFor)
                .apply();
    }
}
