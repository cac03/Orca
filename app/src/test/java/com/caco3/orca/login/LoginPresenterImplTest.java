package com.caco3.orca.login;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksException;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.OrioksResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginPresenterImplTest {

    private LoginPresenter presenter;

    @Mock
    private Orioks mockOrioks;

    @Mock
    private CredentialsManager mockCredentialsManager;

    @Mock
    private OrioksRepository mockRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenterImpl(mockOrioks, mockCredentialsManager, mockRepository);
    }

    @Before
    public void setUp(){
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook(){
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown(){
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void whenInitialLoginIsNotNullSetLoginCalled(){

        final AtomicBoolean setLoginCalled = new AtomicBoolean(false);

        presenter.onViewAttached(new NullLoginView(){
            @Override
            public String getInitialLogin() {
                return "dummy";
            }

            @Override
            public void setLogin(String login) {
                setLoginCalled.set(true);
                System.out.println("setLogin(" + login + ")");
            }
        });

        assertTrue(setLoginCalled.get());
    }

    @Test
    public void whenLoginIsEmptyStringSetLoginIsEmptyStringErrorCalled(){
        final AtomicBoolean setLoginIsEmptyStringErrorCalled = new AtomicBoolean(false);
        presenter.onViewAttached(new NullLoginView(){
            @Override
            public void setLoginIsEmptyStringError() {
                super.setLoginIsEmptyStringError();
                setLoginIsEmptyStringErrorCalled.set(true);
            }
        });

        presenter.attemptToLogIn("", "dummyPassword");

        assertTrue(setLoginIsEmptyStringErrorCalled.get());
    }

    @Test
    public void whenPasswordIsEmptyStringSetPasswordIsEmptyStringErrorCalled(){
        final AtomicBoolean setPasswordIsEmptyStringErrorCalled = new AtomicBoolean(false);
        presenter.onViewAttached(new NullLoginView(){
            @Override
            public void setPasswordIsEmptyStringError() {
                super.setPasswordIsEmptyStringError();
                setPasswordIsEmptyStringErrorCalled.set(true);
            }
        });

        presenter.attemptToLogIn("dummyLogin", "");

        assertTrue(setPasswordIsEmptyStringErrorCalled.get());
    }

    @Test(timeout = 800L)
    public void ifEverythingOkNavigateToLearningActivityCalled() throws Exception {
        final AtomicBoolean navigateToLearningActivityCalled = new AtomicBoolean(false);

        /**
         * LogIn operation is executed in background thread
         */
        final CountDownLatch latch = new CountDownLatch(1);

        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.just(mock(OrioksResponse.class)));

        presenter.onViewAttached(new NullLoginView(){
            @Override
            public void navigateToLearningActivity() {
                super.navigateToLearningActivity();
                /**
                 * Let the test continue
                 */
                latch.countDown();
                navigateToLearningActivityCalled.set(true);
            }
        });

        presenter.attemptToLogIn("dummy", "dummy");
        /**
         * Block until navigateToLearningActivityCalled not called or timeout
         */
        latch.await();

        assertTrue(navigateToLearningActivityCalled.get());
    }

    @Test(timeout = 800L)
    public void whenIoExceptionThrownSayNetworkErrorOccurredCalled() throws Exception {
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.<OrioksResponse>error(new IOException()));
        final AtomicBoolean sayNetworkErrorOccurredCalled = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(1);
        presenter.onViewAttached(new NullLoginView(){
            @Override
            public void sayNetworkErrorOccurred() {
                super.sayNetworkErrorOccurred();
                sayNetworkErrorOccurredCalled.set(true);
                latch.countDown();
            }
        });
        presenter.attemptToLogIn("dummy", "dummy");
        latch.await();

        assertTrue(sayNetworkErrorOccurredCalled.get());
    }

    @Test(timeout = 800L)
    public void whenOrioksExceptionThrownSayIncorrectLoginOrPasswordCalled() throws Exception {
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.<OrioksResponse>error(new OrioksException("", "")));
        final AtomicBoolean sayIncorrectLoginOrPasswordCalled = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(1);
        presenter.onViewAttached(new NullLoginView(){
            @Override
            public void sayLoginOrPasswordIsIncorrect() {
                super.sayLoginOrPasswordIsIncorrect();
                sayIncorrectLoginOrPasswordCalled.set(true);
                latch.countDown();
            }
        });
        presenter.attemptToLogIn("dummy", "dummy");
        latch.await();

        assertTrue(sayIncorrectLoginOrPasswordCalled.get());
    }

    @Test(timeout = 800L)
    public void whenViewReattachedShowProgressCalledIfLogInNotFinished() throws Exception {
        final CountDownLatch viewReattached = new CountDownLatch(1);
        final CountDownLatch responseReturned = new CountDownLatch(1);
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.fromCallable(new Callable<OrioksResponse>() {
                    @Override
                    public OrioksResponse call() throws Exception {
                        viewReattached.await();
                        responseReturned.countDown();
                        return mock(OrioksResponse.class);
                    }
                }));
        presenter.onViewAttached(new NullLoginView());
        presenter.attemptToLogIn("dummy", "dummy");
        presenter.onViewDetached();
        final AtomicBoolean showProgressCalled = new AtomicBoolean(false);
        presenter.onViewAttached(new NullLoginView(){
            @Override
            public void showProgress() {
                super.showProgress();
                showProgressCalled.set(true);
            }
        });
        viewReattached.countDown();

        responseReturned.await();
        assertTrue(showProgressCalled.get());
    }

    @Test(timeout = 800L)
    public void afterSuccessfulLogInCredentialsAreSaved() throws Exception {
        final AtomicBoolean credentialsSaved = new AtomicBoolean(false);
        final CountDownLatch credentialsSavedLatch = new CountDownLatch(1);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                credentialsSaved.set(true);
                credentialsSavedLatch.countDown();
                return null;
            }
        }).when(mockCredentialsManager).saveAndSetAsActive(any(UserCredentials.class));
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.just(mock(OrioksResponse.class)));

        presenter.onViewAttached(new NullLoginView());
        presenter.attemptToLogIn("dummy", "dummy");
        credentialsSavedLatch.await();
        assertTrue(credentialsSaved.get());
    }


    private class NullLoginView implements LoginView {
        @Override
        public void showProgress() {
            System.out.println("showProgress()");
        }

        @Override
        public void hideProgress() {
            System.out.println("hideProgress()");
        }

        @Override
        public void setLoginIsEmptyStringError() {
            System.out.println("setLoginIsEmptyStringError()");
        }

        @Override
        public void setPasswordIsEmptyStringError() {
            System.out.println("setPasswordIsEmptyStringError()");

        }

        @Override
        public void sayLoginOrPasswordIsIncorrect() {
            System.out.println("sayLoginOrPasswordIsIncorrect()");

        }

        @Override
        public void navigateToLearningActivity() {
            System.out.println("navigateToLearningActivity()");

        }

        @Override
        public void sayNetworkErrorOccurred() {
            System.out.println("sayNetworkErrorOccurred()");
        }

        @Override
        public String getInitialLogin() {
            System.out.println("getInitialLogin()");
            return null;
        }

        @Override
        public void setLogin(String login) {
            System.out.println("setLogin(" + login + ")");
        }

        @Override
        public void setPassword(String password) {
            System.out.println("setPassword(" + password + ")");
        }

        @Override
        public void setPresenter(LoginPresenter presenter) {
            System.out.println("setPresenter()");
        }
    }
}
