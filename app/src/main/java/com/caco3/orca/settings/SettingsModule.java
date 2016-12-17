package com.caco3.orca.settings;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {

    @Provides
    @Singleton
    public Settings provideSettings(Context context) {
        return new SettingsSharedPreferencesImpl(context);
    }
}
