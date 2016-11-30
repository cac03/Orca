package com.caco3.orca.entrypoint;


import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.orioks.UserCredentials;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntryPointManagerTest {

    @Test
    public void whenNoUserSignedInLoginActivityStarted(){
        CredentialsManager mockCredentialsManager = mock(CredentialsManager.class);

        when(mockCredentialsManager.getCurrentCredentials())
                .thenReturn(null);


        EntryPointManager entryPointManager = new EntryPointManager(mockCredentialsManager);

        final AtomicBoolean loginActivityStarted = new AtomicBoolean(false);
        entryPointManager.doWork(new EntryPointActivity() {
            @Override
            public void navigateToLoginActivity() {
                loginActivityStarted.set(true);
            }

            @Override
            public void navigateToLearningActivity() {
                fail();
            }
        });

        assertTrue(loginActivityStarted.get());
    }

    @Test
    public void whenThereIsUserSignedInLearningActivityStarted(){
        CredentialsManager mockCredentialsManager = mock(CredentialsManager.class);

        when(mockCredentialsManager.getCurrentCredentials())
                .thenReturn(new UserCredentials("214124", "2151234"));


        EntryPointManager entryPointManager = new EntryPointManager(mockCredentialsManager);

        final AtomicBoolean learningActivityStarted = new AtomicBoolean(false);
        entryPointManager.doWork(new EntryPointActivity() {
            @Override
            public void navigateToLoginActivity() {
                fail();
            }

            @Override
            public void navigateToLearningActivity() {
                learningActivityStarted.set(true);
            }
        });

        assertTrue(learningActivityStarted.get());
    }
}
