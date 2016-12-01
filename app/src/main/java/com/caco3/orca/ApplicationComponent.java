package com.caco3.orca;


import android.content.Context;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.credentials.CredentialsModule;
import com.caco3.orca.data.schedule.ScheduleRepository;
import com.caco3.orca.data.schedule.ScheduleRepositoryModule;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksModule;
import com.caco3.orca.scheduleapi.ScheduleApi;
import com.caco3.orca.scheduleapi.ScheduleApiModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OrioksModule.class,
        CredentialsModule.class,
        ApplicationModule.class,
        ScheduleApiModule.class,
        ScheduleRepositoryModule.class})
public interface ApplicationComponent {

    ScheduleRepository getScheduleRepository();

    ScheduleApi getScheduleApi();

    Orioks getOrioks();

    CredentialsManager getCredentialsManager();

    Context getContext();
}
