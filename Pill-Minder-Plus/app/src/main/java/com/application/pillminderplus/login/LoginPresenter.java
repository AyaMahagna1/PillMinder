package com.application.pillminderplus.login;

import android.app.Activity;

import com.application.pillminderplus.model.User;
import com.application.pillminderplus.network.NetworkLoginDelegate;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//Sign in with firebase and get response
public class LoginPresenter implements  NetworkLoginDelegate {
    private final LoginFragment viewFragment;
    private final LoginRepository repository;
    public LoginPresenter(LoginFragment viewFragment, LoginRepository repository) {
        this.viewFragment = viewFragment;
        this.repository = repository;
    }

    public void signInWithEmailAndPassword(String email, String password) {
        repository.signInWithEmailAndPassword(this, email, password);
    }

    public GoogleSignInClient getGoogleSignInClient(Activity activity) {
        return repository.getGoogleSignInClient(activity);
    }

    public void signInWithGoogle(String idToken) {
        repository.signInWithGoogle(this, idToken);
    }

    @Override
    public void onResponse(String userId) {
        repository.setUserLogin(true);
        repository.setUserId(userId);
        viewFragment.hideProgressbar();
        viewFragment.navigateToHomeScreen();
    }

    @Override
    public void onResponse(User user) {
        repository.insertUserToRoom(user);
        onResponse(user.getUserId());
    }

    @Override
    public void onFailure(String error) {
        viewFragment.onError(error);
        viewFragment.hideProgressbar();
    }
}
