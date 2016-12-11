package com.caco3.orca.schedule;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SchedulePreferencesModule {
    @Provides
    @Singleton
    public SchedulePreferences provideSchedulePreferences(Context context) {
        return new SchedulePreferencesImpl(context);
    }
}
