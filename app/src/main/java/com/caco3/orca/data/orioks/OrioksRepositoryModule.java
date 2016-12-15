package com.caco3.orca.data.orioks;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class OrioksRepositoryModule {

    @Provides
    @Singleton
    public OrioksRepository provideOrioksRepository(Context context) {
        return new SerializedOrioksRepositoryImpl(context);
    }
}
