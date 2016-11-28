package com.caco3.orca;


import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksModule;

import org.mockito.Mockito;

public class MockApplication extends OrcaApp {

    @Override
    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .orioksModule(new OrioksModule(){
                    @Override
                    public Orioks provideOrioks() {
                        return Mockito.mock(Orioks.class);
                    }
                });
    }
}
