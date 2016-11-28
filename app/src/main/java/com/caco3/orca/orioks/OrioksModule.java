package com.caco3.orca.orioks;

import com.caco3.orca.orioks.model.OrioksResponse;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Observable;


@Module
public class OrioksModule {

    @Provides
    @Singleton
    public Orioks provideOrioks(){
        return new Orioks() {

            @Override
            public Observable<OrioksResponse> getResponseForCurrentSemester(UserCredentials credentials) {
                throw new AssertionError(
                        "getResponseForCurrentSemester(UserCredentials ) is not implemented");
            }

            @Override
            public Observable<OrioksResponse> getResponse(UserCredentials credentials, int semester) {
                throw new AssertionError(
                        "getResponseForCurrentSemester(UserCredentials, int) is not implemented");
            }
        };
    }
}
