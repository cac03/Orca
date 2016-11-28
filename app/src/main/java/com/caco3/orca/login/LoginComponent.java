package com.caco3.orca.login;


import com.caco3.orca.ApplicationComponent;

import dagger.Component;

/**
 * Custom dagger component.
 */
@LoginScope
@Component(dependencies = ApplicationComponent.class)
/*package*/ interface LoginComponent {

    void inject(LoginFragment fragment);
}
