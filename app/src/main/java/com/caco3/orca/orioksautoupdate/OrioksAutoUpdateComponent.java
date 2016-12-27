package com.caco3.orca.orioksautoupdate;

import com.caco3.orca.ApplicationComponent;

import dagger.Component;


@OrioksAutoUpdateScope
@Component(dependencies = ApplicationComponent.class)
public interface OrioksAutoUpdateComponent {

    void inject(OrioksAutoUpdateService autoUpdateService);
}
