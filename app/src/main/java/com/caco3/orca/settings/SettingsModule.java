package com.caco3.orca.settings;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {

    private SettingsSharedPreferencesImpl settingsSharedPreferences;


    @Provides
    @Singleton
    public Settings provideSettings(Context context) {
        return createOrReturnSettingsSharedPreferences(context);
    }

    @Provides
    @SettingsScope
    public SettingsFragment.SharedPreferencesChangedListener provideSharedPreferencesChangedListener(Context context){
        return createOrReturnSettingsSharedPreferences(context);
    }

    private SettingsSharedPreferencesImpl createOrReturnSettingsSharedPreferences(Context context){
        if (settingsSharedPreferences == null) {
            synchronized (SettingsSharedPreferencesImpl.class) {
                if (settingsSharedPreferences == null) {
                    settingsSharedPreferences = new SettingsSharedPreferencesImpl(context);
                }
            }
        }

        return settingsSharedPreferences;
    }
}
