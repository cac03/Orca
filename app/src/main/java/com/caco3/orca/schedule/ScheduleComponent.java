package com.caco3.orca.schedule;

import com.caco3.orca.ApplicationComponent;

import dagger.Component;

/**
 * Custom dagger component for {@link ScheduleScope}
 */
@ScheduleScope
@Component(modules = ScheduleModule.class, dependencies = ApplicationComponent.class)
/*package*/ interface ScheduleComponent {
    void inject(ScheduleFragment fragment);
}
