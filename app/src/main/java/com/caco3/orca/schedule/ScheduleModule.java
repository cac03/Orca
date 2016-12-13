package com.caco3.orca.schedule;

import com.caco3.orca.data.schedule.ScheduleRepository;
import com.caco3.orca.scheduleapi.ScheduleApi;

import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleModule {

    @Provides
    @ScheduleScope
    SchedulePresenter provideSchedulePresenter(ScheduleApi scheduleApi,
                                               ScheduleRepository scheduleRepository,
                                               SchedulePreferences schedulePreferences){
        return new SchedulePresenterImpl(scheduleApi, scheduleRepository, schedulePreferences);
    }
}
