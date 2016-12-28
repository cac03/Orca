package com.caco3.orca.disciplinedetails;

import com.caco3.orca.ApplicationComponent;

import dagger.Component;


@DisciplineDetailsScope
@Component(dependencies = ApplicationComponent.class)
public interface DisciplineDetailsComponent {

    void inject(DisciplineDetailsFragment fragment);
}
