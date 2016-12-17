package com.caco3.orca.entrypoint;

import com.caco3.orca.ui.BaseActivity;

/**
 * Launcher activity must implement this interface so the
 */
/*package*/ interface EntryPointActivity {

    void startActivity(Class<? extends BaseActivity> activityClass);
}
