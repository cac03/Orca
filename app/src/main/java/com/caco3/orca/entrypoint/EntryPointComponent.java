package com.caco3.orca.entrypoint;

import com.caco3.orca.ApplicationComponent;

import dagger.Component;

@EntryPointScope
@Component(dependencies = ApplicationComponent.class)
/*package*/ interface EntryPointComponent {

    void inject(SplashActivity splashActivity);
}
