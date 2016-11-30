package com.caco3.orca.entrypoint;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.util.Preconditions;

import javax.inject.Inject;

/**
 * Decides which activity to launch first and performs some bootstrap work if necessary
 */
/*package*/ class EntryPointManager {


    // not null, injected in c-tor
    private CredentialsManager credentialsManager;

    @Inject
    /*package*/ EntryPointManager(CredentialsManager credentialsManager) {
        this.credentialsManager = Preconditions.checkNotNull(credentialsManager);
    }

    /*package*/ void doWork(EntryPointActivity entryPointActivity){
        if (credentialsManager.getCurrentCredentials() == null) {
            // no user signed in
            entryPointActivity.navigateToLoginActivity();
        } else {
            entryPointActivity.navigateToLearningActivity();
        }
    }
}
