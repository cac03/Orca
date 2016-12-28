package com.caco3.orca.settings;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.caco3.orca.R;
import com.caco3.orca.learning.LearningActivity;
import com.caco3.orca.schedule.ScheduleActivity;
import com.caco3.orca.settings.settingschanges.OrioksAutoUpdateDisabledChange;
import com.caco3.orca.settings.settingschanges.OrioksAutoUpdateEnabledChange;
import com.caco3.orca.ui.BaseActivity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Concrete {@link Settings} implementation where all settings are stored
 * using {@link android.content.SharedPreferences}
 */
/*package*/ class SettingsSharedPreferencesImpl
        implements Settings, SettingsFragment.SharedPreferencesChangedListener {
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
    private static final String DEFAULT_ACTIVITY_INDEX = "0";
    /**
     * {@link android.content.SharedPreferences} key where index of start activity is stored
     */
    private final String START_ACTIVITY_KEY;


    /**
     * {@link android.content.SharedPreferences} key for boolean indicating whether the app is
     * launched for the first time
     */
    private static final String FIRST_RUN_KEY = "first_run";

    /**
     * Boolean indicating whether the app should send notifications after background update
     */
    private final String RECEIVE_NOTIFICATIONS_KEY;

    /**
     * Boolean indicating whether the app should update info from orioks in background
     */
    private final String ORIOKS_AUTO_UPDATE_ENABLED_KEY;

    @NonNull // injected in c-tor
    private final Context context;

    @Inject
    /*package*/ SettingsSharedPreferencesImpl(@NonNull Context context) {
        this.context = context;
        // final key strings initialization
        START_ACTIVITY_KEY = context.getString(R.string.prefs_start_activity_key);
        RECEIVE_NOTIFICATIONS_KEY = context.getString(R.string.prefs_receive_notifications_about_points_changes_key);
        ORIOKS_AUTO_UPDATE_ENABLED_KEY = context.getString(R.string.prefs_orioks_auto_update_key);
    }

    @Override
    public Class<? extends BaseActivity> getStartActivity() {
        int index = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(START_ACTIVITY_KEY, DEFAULT_ACTIVITY_INDEX));
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
                .getBoolean(FIRST_RUN_KEY, true);
    }

    @Override
    public void setFirstRun(boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(FIRST_RUN_KEY, value)
                .apply();
    }

    @Override
    public void onPreferenceChanged(String key) {
        Timber.d("Settings changed. Key = %s", key);
        if (key.equals(ORIOKS_AUTO_UPDATE_ENABLED_KEY)) {
            boolean enabled = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(ORIOKS_AUTO_UPDATE_ENABLED_KEY, false);
            new OrioksAutoUpdateDisabledChange(context).apply();
            Timber.i("OrioksAutoUpdate disabled");
            if (enabled) {
                Timber.i("OrioksAutoUpdate enabled with 8h interval");
                new OrioksAutoUpdateEnabledChange(context, TimeUnit.HOURS.toMillis(8)).apply();
            }
        } else if (key.equals(RECEIVE_NOTIFICATIONS_KEY)) {
            // do nothing for now
        } else if (key.equals(START_ACTIVITY_KEY)) {
            // do nothing.
        } else {
            Timber.e("Unknown key(%s) changed in default shared preferences.", key);
            throw new IllegalArgumentException("Unknown key(" + key
                    + ") changed in default shared preferences.");
        }
    }
}
