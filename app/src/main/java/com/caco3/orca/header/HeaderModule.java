package com.caco3.orca.header;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class HeaderModule {

    @Singleton
    @Provides
    public HeaderPresenter provideHeaderPresenter(OrioksRepository repository, CredentialsManager credentialsManager){
        return new HeaderPresenterImpl(repository, credentialsManager);
    }
}
