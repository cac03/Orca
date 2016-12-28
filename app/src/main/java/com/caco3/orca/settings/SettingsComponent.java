package com.caco3.orca.settings;


import com.caco3.orca.ApplicationComponent;

import dagger.Component;

@SettingsScope
@Component(dependencies = ApplicationComponent.class, modules = SettingsModule.class)
public interface SettingsComponent {

    void inject(SettingsFragment fragment);
}
