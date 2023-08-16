package com.application.pillminderplus.forgotpassword;

import com.application.pillminderplus.network.NetworkDelegate;
//Reset password
public class ForgotPasswordPresenter implements NetworkDelegate {
    private final ForgotPasswordFragment viewFragment;
    private final ForgotPasswordRepository repository;
    public ForgotPasswordPresenter(ForgotPasswordFragment viewFragment, ForgotPasswordRepository repository) {
        this.viewFragment = viewFragment;
        this.repository = repository;
    }

    public void sendPasswordResetEmail(String email) {
        repository.sendPasswordResetEmail(this, email);
    }
    @Override
    public void onResponse() {
        viewFragment.linkSentSuccessfully();
        viewFragment.hideProgressbar();
        viewFragment.navigateToLoginScreen();
    }

    @Override
    public void onFailure(String error) {
        viewFragment.onError(error);
        viewFragment.hideProgressbar();
    }
}
