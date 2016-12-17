package com.caco3.orca.settings;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.caco3.orca.learning.LearningActivity;
import com.caco3.orca.schedule.ScheduleActivity;
import com.caco3.orca.ui.BaseActivity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Concrete {@link Settings} implementation where all settings are stored
 * using {@link android.content.SharedPreferences}
 */
/*package*/ class SettingsSharedPreferencesImpl implements Settings {
    /**
     * An array of activities that may be launched when the app starts.
     * In {@link android.content.SharedPreferences} we will store index
     * of an activity
     */
    private static final List<Class<? extends BaseActivity>> ACTIVITIES
            = Arrays.asList(LearningActivity.class, ScheduleActivity.class);

    /**
     * Index of activity in {@link #ACTIVITIES} list that must be launched if no
     * other activity was specified via {@link #setStartActivity(Class)}
     */
    private static final int DEFAULT_ACTIVITY_INDEX = 0;
    /**
     * {@link android.content.SharedPreferences} key where index of start activity is stored
     */
    private static final String START_ACTIVITY_KEY = "start_activity";

    /**
     * {@link android.content.SharedPreferences} key for boolean indicating whether the app is
     * launched for the first time
     */
    private static final String FIRST_RUN_KEY = "first_run";

    @NonNull // injected in c-tor
    private final Context context;

    @Inject
    /*package*/ SettingsSharedPreferencesImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public Class<? extends BaseActivity> getStartActivity() {
        int index = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(START_ACTIVITY_KEY, DEFAULT_ACTIVITY_INDEX);
        if (index < 0 || index >= ACTIVITIES.size()) {
            throw new IllegalStateException("Stored index of activity is out of bounds of activities array"
                    + " ACTIVITIES.size() = " + ACTIVITIES.size() + " index = " + index);
        }

        return ACTIVITIES.get(index);
    }

    @Override
    public void setStartActivity(Class<? extends BaseActivity> activityClass) {
        int index = ACTIVITIES.indexOf(activityClass);
        if (index == -1) {
            throw new IllegalArgumentException(activityClass.getSimpleName()
                    + " is not registered in activities array or this " +
                    "activity cannot be selected as start activity.");
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(START_ACTIVITY_KEY, index)
                .apply();
    }

    @Override
    public boolean isFirstRun() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(FIRST_RUN_KEY, false);
    }

    @Override
    public void setFirstRun(boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(FIRST_RUN_KEY, value)
                .apply();
    }
}
