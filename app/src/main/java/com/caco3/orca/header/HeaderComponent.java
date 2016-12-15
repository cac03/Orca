package com.caco3.orca.header;

import com.caco3.orca.ApplicationComponent;
import com.caco3.orca.ui.BaseActivity;

import dagger.Component;


@HeaderScope
@Component(dependencies = ApplicationComponent.class)
/*package*/ interface HeaderComponent {

    void inject(BaseActivity activity);
}
