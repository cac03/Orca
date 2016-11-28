package com.caco3.orca.login;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.caco3.orca.MockApplication;
import com.caco3.orca.R;
import com.caco3.orca.orioks.LoginOrPasswordIncorrectException;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.OrioksResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.Callable;


import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private Orioks orioks;

    @Rule
    public IntentsTestRule<LoginActivity> testRule
            = new IntentsTestRule<>(LoginActivity.class);

    private static final String LOGIN_DUMMY = "dummy login";
    private static final String PASSWORD_DUMMY = "dummy password";

    @Before
    public void initOrioks(){

        orioks = MockApplication.get(testRule.getActivity())
                .getApplicationComponent().getOrioks();
    }

    @Test
    public void whenIoExceptionThrownToastShown() {

        when(orioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.fromCallable(new Callable<OrioksResponse>() {
                    @Override
                    public OrioksResponse call() throws Exception {
                        // do delay
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        throw new IOException();
                    }
                }));

        onView(withId(R.id.login_frag_login))
                .perform(typeText(LOGIN_DUMMY));

        onView(withId(R.id.login_frag_password))
                .perform(typeText(PASSWORD_DUMMY));

        onView(withId(R.id.login_frag_sign_in_btn))
                .perform(click());

        onView(withText(R.string.network_error_occurred))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenIncorrectCredentialsProvidedToastIsShown(){
        when(orioks.getResponseForCurrentSemester(any(UserCredentials.class)))
                .thenReturn(Observable.fromCallable(new Callable<OrioksResponse>() {
                    @Override
                    public OrioksResponse call() throws Exception {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }

                        throw new LoginOrPasswordIncorrectException();
                    }
                }));

        typeDummyLogin();
        typeDummyPassword();
        clickSignInButton();
        onView(withText(R.string.login_or_password_incorrect))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private static void typeDummyLogin(){
        onView(withId(R.id.login_frag_login))
                .perform(typeText(LOGIN_DUMMY));
    }

    private static void typeDummyPassword(){
        onView(withId(R.id.login_frag_password))
                .perform(typeText(PASSWORD_DUMMY));
    }

    private static void clickSignInButton(){
        onView(withId(R.id.login_frag_sign_in_btn))
                .perform(click());
    }
}
