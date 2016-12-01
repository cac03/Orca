package com.caco3.orca.scheduleapi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for {@link ScheduleApi}
 */
@Module
public class ScheduleApiModule {

    @Provides
    @Singleton
    ScheduleApi provideScheduleApi(){
        return ScheduleApiFactory.getScheduleApi();
    }
}
