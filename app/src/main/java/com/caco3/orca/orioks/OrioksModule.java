package com.caco3.orca.orioks;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class OrioksModule {

    @Provides
    @Singleton
    public Orioks provideOrioks() {
        return new OrioksImpl();
    }
}
