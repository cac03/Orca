package com.caco3.orca;


import android.content.Context;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.credentials.CredentialsModule;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksModule;

import org.mockito.Mockito;

public class MockApplication extends OrcaApp {

    @Override
    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .orioksModule(new OrioksModule(){
                    @Override
                    public Orioks provideOrioks() {
                        return Mockito.mock(Orioks.class);
                    }
                })
                .credentialsModule(new CredentialsModule(){
                    @Override
                    public CredentialsManager provideCredentialsManager(Context context) {
                        return Mockito.mock(CredentialsManager.class);
                    }
                });
    }
}
