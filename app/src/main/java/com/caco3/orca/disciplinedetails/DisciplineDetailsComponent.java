package com.caco3.orca.disciplinedetails;

import com.caco3.orca.ApplicationComponent;

import dagger.Component;


@DisciplineDetailsScope
@Component(dependencies = ApplicationComponent.class)
/*package*/ interface DisciplineDetailsComponent {

    void inject(DisciplineDetailsFragment fragment);
}
