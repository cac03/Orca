package com.caco3.orca.learning;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.caco3.orca.ApplicationComponent;
import com.caco3.orca.OrcaApp;
import com.caco3.orca.R;
import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.orioks.LoginOrPasswordIncorrectException;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksResponseGenerator;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.OrioksResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LearningActivityTest {

    private Orioks mockOrioks;

    private CredentialsManager mockCredentialsManager;

    @Rule
    public ActivityTestRule<LearningActivity> activityTestRule
            = new ActivityTestRule<>(LearningActivity.class);


    @Before
    public void initMocks(){

        ApplicationComponent applicationComponent
                = OrcaApp.get(activityTestRule.getActivity())
                .getApplicationComponent();

        mockOrioks = applicationComponent.getOrioks();
        mockCredentialsManager = applicationComponent.getCredentialsManager();
    }


    @Test
    public void whenCredentialsWereIncorrectUserNotified(){
        when(mockCredentialsManager.getCurrentCredentials())
                .thenReturn(new UserCredentials("dummy", "dummy"));

        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .then(new Answer<Observable<OrioksResponse>>() {
                    @Override
                    public Observable<OrioksResponse> answer(InvocationOnMock invocation) throws Throwable {
                        return Observable.error(new LoginOrPasswordIncorrectException());
                    }
                });

        onView(withId(R.id.learning_refresh_layout))
                .perform(swipeDown());

        onView(withText(R.string.login_or_password_incorrect))
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenNetworkErrorOccurredUserNotified(){
        when(mockCredentialsManager.getCurrentCredentials())
                .thenReturn(new UserCredentials("dummy", "dummy"));

        when(mockOrioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .then(new Answer<Observable<OrioksResponse>>() {
                    @Override
                    public Observable<OrioksResponse> answer(InvocationOnMock invocation) throws Throwable {
                        return Observable.error(new IOException());
                    }
                });

        onView(withId(R.id.learning_refresh_layout))
                .perform(swipeDown());

        onView(withText(R.string.network_error_occurred))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
}
