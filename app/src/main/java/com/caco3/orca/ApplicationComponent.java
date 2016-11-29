package com.caco3.orca;


import android.content.Context;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.credentials.CredentialsModule;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OrioksModule.class, CredentialsModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    Orioks getOrioks();

    CredentialsManager getCredentialsManager();

    Context getContext();
}
