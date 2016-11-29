package com.caco3.orca.learning;

import com.caco3.orca.ApplicationComponent;

import dagger.Component;

@LearningScope
@Component(dependencies = ApplicationComponent.class)
/*package*/ interface LearningComponent {

    void inject(LearningFragment fragment);
}
