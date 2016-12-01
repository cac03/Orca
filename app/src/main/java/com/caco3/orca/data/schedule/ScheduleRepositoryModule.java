package com.caco3.orca.data.schedule;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger repository module for {@link ScheduleRepository}
 */
@Module
public class ScheduleRepositoryModule {

    @Provides
    @Singleton
    public ScheduleRepository provideScheduleRepository(Context context){
        return new ScheduleRepositoryDbImpl(context);
    }
}
