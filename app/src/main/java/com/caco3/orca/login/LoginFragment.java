package com.caco3.orca.login;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.R;
import com.caco3.orca.learning.LearningActivity;
import com.caco3.orca.util.Preconditions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Here user is able to enter his credentials to get access to other major app features
 * Implements {@link LoginView} (mvp)
 * Controlled by {@link LoginPresenter}
 */
public class LoginFragment extends Fragment implements LoginView {
    /**
     * A key for String in arguments for this fragment. That can be retrieved via
     * {@link Fragment#getArguments()}.
     * Arguments will have string mapped by this key if this fragment was created via
     * {@link #createAndSetLogin(String)}
     */
    private static final String LOGIN_KEY = "login";


    @BindView(R.id.login_frag_login)
    EditText loginView;

    @BindView(R.id.login_frag_password)
    EditText passwordView;

    /**
     * Presenter associated with this view.
     * Will be injected in {@link #onViewCreated(View, Bundle)}
     */
    @Inject
    LoginPresenterImpl presenter;

    /**
     * Keep reference to dialog displaying progress of signing in
     * so we can dismiss it if necessary
     * @see #showProgress()
     * @see #hideProgress()
     */
    private ProgressDialog signingInDialog;


    public static LoginFragment createAndSetLogin(String login){
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle(1);
        args.putString(LOGIN_KEY, login);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DaggerLoginComponent.builder()
                .applicationComponent(OrcaApp.get(getContext()).getApplicationComponent())
                .build()
                .inject(this);

        presenter.onViewAttached(this);
    }

    @Override
    public void onDestroyView() {
        presenter.onViewDetached();

        /**
         * It will be shown by presenter when view will be reattached
         */
        if (signingInDialog != null) {
            hideProgress();
        }

        super.onDestroyView();
    }

    @Override
    public void showProgress() {
        if (signingInDialog != null) {
            throw new IllegalStateException(
                    "Attempt to create dialog progress when another one is created");
        }

        signingInDialog = new ProgressDialog(getContext());
        signingInDialog.setIndeterminate(true);
        signingInDialog.setMessage(getString(R.string.signing_in));
        signingInDialog.setCanceledOnTouchOutside(false);
        signingInDialog.show();
    }

    @Override
    public void hideProgress() {
        if (signingInDialog == null) {
            throw new IllegalStateException(
                    "Attempt to close progress dialog, but it was not created");
        }
        signingInDialog.dismiss();

        signingInDialog = null;
    }

    @Override
    public void setLoginIsEmptyStringError() {
        loginView.setError(getString(R.string.login_cannot_be_empty_string));
    }

    @Override
    public void setPasswordIsEmptyStringError() {
        passwordView.setError(getString(R.string.password_cannot_be_empty_string));
    }

    @OnClick(R.id.login_frag_sign_in_btn)
    void attemptToLogIn(){
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();

        presenter.attemptToLogIn(login, password);
    }

    @Override
    public void sayLoginOrPasswordIsIncorrect() {
        Toast.makeText(getContext(), R.string.login_or_password_incorrect, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(LoginPresenter presenter) {
        this.presenter = (LoginPresenterImpl)presenter;
    }

    @Override
    public void navigateToLearningActivity() {
        Activity activity = getActivity();
        activity.startActivity(new Intent(activity, LearningActivity.class));
        activity.finish();
    }

    @Override
    public void sayNetworkErrorOccurred() {
        Toast.makeText(getContext(), R.string.network_error_occurred, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getInitialLogin() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(LOGIN_KEY)) {
            return args.getString(LOGIN_KEY);
        } else {
            return null;
        }
    }

    @Override
    public void setLogin(String login) {
        loginView.setText(login);
    }

    @Override
    public void setPassword(String password) {
        passwordView.setText(password);
    }
}
