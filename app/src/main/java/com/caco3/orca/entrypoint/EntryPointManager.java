package com.caco3.orca.entrypoint;

import com.caco3.orca.settings.Settings;

import javax.inject.Inject;

/**
 * Decides which activity to launch first and performs some bootstrap work if necessary
 */
/*package*/ class EntryPointManager {


    // not null, injected in c-tor
    private Settings settings;

    @Inject
    /*package*/ EntryPointManager(Settings settings) {
        this.settings = settings;
    }

    /*package*/ void doWork(EntryPointActivity entryPointActivity){
        entryPointActivity.startActivity(settings.getStartActivity());
    }
}
