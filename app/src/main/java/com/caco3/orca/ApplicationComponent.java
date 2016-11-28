package com.caco3.orca;


import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {OrioksModule.class})
public interface ApplicationComponent {

    Orioks getOrioks();
}
