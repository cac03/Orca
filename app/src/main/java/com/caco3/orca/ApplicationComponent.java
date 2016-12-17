package com.caco3.orca;


import android.content.Context;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.credentials.CredentialsModule;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.data.orioks.OrioksRepositoryModule;
import com.caco3.orca.data.schedule.ScheduleRepository;
import com.caco3.orca.data.schedule.ScheduleRepositoryModule;
import com.caco3.orca.header.HeaderModule;
import com.caco3.orca.header.HeaderPresenter;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksModule;
import com.caco3.orca.schedule.ScheduleModule;
import com.caco3.orca.schedule.SchedulePreferences;
import com.caco3.orca.schedule.SchedulePreferencesModule;
import com.caco3.orca.scheduleapi.ScheduleApi;
import com.caco3.orca.scheduleapi.ScheduleApiModule;
import com.caco3.orca.settings.Settings;
import com.caco3.orca.settings.SettingsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OrioksModule.class,
        CredentialsModule.class,
        ApplicationModule.class,
        ScheduleApiModule.class,
        ScheduleRepositoryModule.class,
        SchedulePreferencesModule.class,
        OrioksRepositoryModule.class,
        HeaderModule.class,
        SettingsModule.class})
public interface ApplicationComponent {

    Settings getSettings();

    HeaderPresenter getHeaderPresenter();

    OrioksRepository getOrioksRepository();

    SchedulePreferences getSchedulePreferences();

    ScheduleRepository getScheduleRepository();

    ScheduleApi getScheduleApi();

    Orioks getOrioks();

    CredentialsManager getCredentialsManager();

    Context getContext();
}
