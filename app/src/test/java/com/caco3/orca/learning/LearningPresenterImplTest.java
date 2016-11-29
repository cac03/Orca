package com.caco3.orca.learning;


import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.orioks.LoginOrPasswordIncorrectException;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.OrioksResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LearningPresenterImplTest {

    private static CredentialsManager mockCredentialsManager;

    private static Orioks mockOrioks;

    private static final LearningView NULL_LEARNING_VIEW
            = new LearningView() {
        @Override
        public void setItems(List<Discipline> disciplineList) {
        }

        @Override
        public void sayIncorrectLoginOrPassword() {
        }

        @Override
        public void sayNetworkErrorOccurred() {
        }

        @Override
        public void setRefreshing(boolean refreshing) {
        }

        @Override
        public void navigateToDisciplineDetailsActivity(Discipline discipline) {
        }

        @Override
        public void navigateToLoginActivity() {
        }

        @Override
        public void setPresenter(LearningPresenter presenter) {
        }
    };

    @Before
    public void init() {
        mockCredentialsManager = mock(CredentialsManager.class);
        when(mockCredentialsManager.getCurrentCredentials())
                .thenReturn(new UserCredentials("dummy", "dummy"));

        mockOrioks = mock(Orioks.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenCredentialsIncorrectNotifyUserAndSetRefreshingFalseCalled(){
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.<OrioksResponse>error(new LoginOrPasswordIncorrectException()));

        LearningPresenterImpl presenter
                = new LearningPresenterImpl(mockOrioks, mockCredentialsManager);

        final AtomicBoolean notifyUserCalled = new AtomicBoolean(false);
        final AtomicBoolean setRefreshingFalseCalled = new AtomicBoolean();

        presenter.onViewAttached(new LearningView() {
            @Override
            public void setItems(List<Discipline> disciplineList) {
                fail();
            }

            @Override
            public void sayIncorrectLoginOrPassword() {
                notifyUserCalled.set(true);
            }

            @Override
            public void sayNetworkErrorOccurred() {
                fail();
            }

            @Override
            public void setRefreshing(boolean refreshing) {
                if (!refreshing) {
                    setRefreshingFalseCalled.set(true);
                } else {
                    fail();
                }
            }

            @Override
            public void navigateToDisciplineDetailsActivity(Discipline discipline) {
                fail();
            }

            @Override
            public void navigateToLoginActivity() {
                fail();
            }

            @Override
            public void setPresenter(LearningPresenter presenter) {
                fail();
            }
        });

        presenter.onRefreshRequest();

        assertTrue(notifyUserCalled.get() && setRefreshingFalseCalled.get());
    }

    @Test
    public void whenIoExceptionThrownUserNotified(){
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.<OrioksResponse>error(new IOException()));

        LearningPresenterImpl presenter
                = new LearningPresenterImpl(mockOrioks, mockCredentialsManager);

        final AtomicBoolean notifyUserCalled
                = new AtomicBoolean(false);
        final AtomicBoolean setRefreshingFalseCalled
                = new AtomicBoolean(false);
        presenter.onViewAttached(new LearningView() {
            @Override
            public void setItems(List<Discipline> disciplineList) {
                fail();
            }

            @Override
            public void sayIncorrectLoginOrPassword() {
                fail();
            }

            @Override
            public void sayNetworkErrorOccurred() {
                notifyUserCalled.set(true);
            }

            @Override
            public void setRefreshing(boolean refreshing) {
                if (!refreshing) {
                    setRefreshingFalseCalled.set(true);
                } else {
                    fail();
                }
            }

            @Override
            public void navigateToDisciplineDetailsActivity(Discipline discipline) {
                fail();
            }

            @Override
            public void navigateToLoginActivity() {
                fail();
            }

            @Override
            public void setPresenter(LearningPresenter presenter) {
                fail();
            }
        });

        presenter.onRefreshRequest();

        assertTrue(setRefreshingFalseCalled.get() && notifyUserCalled.get());
    }

    @Test
    public void whenResponseOkSetItemsCalled(){
        OrioksResponse mockOrioksResponse = mock(OrioksResponse.class);

        when(mockOrioksResponse.getDisciplines()).thenReturn(new ArrayList<Discipline>());

        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.just(mockOrioksResponse));

        LearningPresenterImpl presenter
                = new LearningPresenterImpl(mockOrioks, mockCredentialsManager);

        final AtomicBoolean setItemsCalled = new AtomicBoolean(false);
        final AtomicBoolean setRefreshingFalseCalled = new AtomicBoolean(false);
        presenter.onViewAttached(new LearningView() {
            @Override
            public void setItems(List<Discipline> disciplineList) {
                setItemsCalled.set(true);
            }

            @Override
            public void sayIncorrectLoginOrPassword() {
                fail();
            }

            @Override
            public void sayNetworkErrorOccurred() {
                fail();
            }

            @Override
            public void setRefreshing(boolean refreshing) {
                if (!refreshing) {
                    setRefreshingFalseCalled.set(true);
                } else {
                    fail();
                }
            }

            @Override
            public void navigateToDisciplineDetailsActivity(Discipline discipline) {
                fail();
            }

            @Override
            public void navigateToLoginActivity() {
                fail();
            }

            @Override
            public void setPresenter(LearningPresenter presenter) {
                fail();
            }
        });

        presenter.onRefreshRequest();

        assertTrue(setItemsCalled.get() && setRefreshingFalseCalled.get());
    }

    @Test
    public void whenOnReLogInRequestCalledNavigateToLoginActivityCalled(){
        LearningPresenterImpl presenter
                = new LearningPresenterImpl(mockOrioks, mockCredentialsManager);

        final AtomicBoolean navigateToLoginActivityCalled = new AtomicBoolean(false);
        presenter.onViewAttached(new LearningView() {
            @Override
            public void setItems(List<Discipline> disciplineList) {
            }

            @Override
            public void sayIncorrectLoginOrPassword() {
            }

            @Override
            public void sayNetworkErrorOccurred() {
            }

            @Override
            public void setRefreshing(boolean refreshing) {
            }

            @Override
            public void navigateToDisciplineDetailsActivity(Discipline discipline) {
                fail();
            }

            @Override
            public void navigateToLoginActivity() {
                navigateToLoginActivityCalled.set(true);
            }

            @Override
            public void setPresenter(LearningPresenter presenter) {
                fail();
            }
        });

        presenter.onReLogInRequest();

        assertTrue(navigateToLoginActivityCalled.get());
    }

    @Test(expected = IllegalStateException.class)
    public void whenNoCredentialsSavedExceptionThrown(){
        CredentialsManager manager = mock(CredentialsManager.class);
        when(manager.getCurrentCredentials()).thenReturn(null);

        LearningPresenterImpl presenter = new LearningPresenterImpl(mockOrioks, manager);
        presenter.onRefreshRequest();
    }

    @Test
    public void whenViewReattachedWithNotCompletedSubscriptionSetRefreshingTrueCalled(){
        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .then(new Answer<Observable<OrioksResponse>>() {
                    @Override
                    public Observable<OrioksResponse> answer(InvocationOnMock invocation) throws Throwable {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                        return Observable.empty();
                    }
                });

        LearningPresenterImpl presenter
                = new LearningPresenterImpl(mockOrioks, mockCredentialsManager);

        presenter.onViewAttached(NULL_LEARNING_VIEW);

        presenter.onRefreshRequest();

        presenter.onViewDetached();

        final AtomicBoolean setRefreshingTrueCalled = new AtomicBoolean(false);

        presenter.onViewAttached(new LearningView() {
            @Override
            public void setItems(List<Discipline> disciplineList) {
            }

            @Override
            public void sayIncorrectLoginOrPassword() {
            }

            @Override
            public void sayNetworkErrorOccurred() {
            }

            @Override
            public void setRefreshing(boolean refreshing) {
                if (refreshing) {
                    setRefreshingTrueCalled.set(true);
                } else {
                    fail();
                }
            }

            @Override
            public void navigateToDisciplineDetailsActivity(Discipline discipline) {
                fail();
            }

            @Override
            public void navigateToLoginActivity() {
                fail();
            }

            @Override
            public void setPresenter(LearningPresenter presenter) {
                fail();
            }
        });

        assertTrue(setRefreshingTrueCalled.get());
    }

    @Test
    public void whenItemClickedNavigateToDisciplineDetailsActivityCalled(){
        LearningPresenterImpl presenter
                = new LearningPresenterImpl(mockOrioks, mockCredentialsManager);

        final AtomicBoolean navigateToDisciplineActivityCalled = new AtomicBoolean(false);
        presenter.onViewAttached(new LearningView() {
            @Override
            public void setItems(List<Discipline> disciplineList) {
            }

            @Override
            public void sayIncorrectLoginOrPassword() {
            }

            @Override
            public void sayNetworkErrorOccurred() {
            }

            @Override
            public void setRefreshing(boolean refreshing) {
            }

            @Override
            public void navigateToDisciplineDetailsActivity(Discipline discipline) {
                navigateToDisciplineActivityCalled.set(true);
            }

            @Override
            public void navigateToLoginActivity() {
            }

            @Override
            public void setPresenter(LearningPresenter presenter) {
            }
        });

        presenter.onDisciplineClicked(null);

        assertTrue(navigateToDisciplineActivityCalled.get());
    }
}
