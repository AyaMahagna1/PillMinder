package com.application.pillminderplus.splash;

public class SplashPresenter {

    private final SplashFragment viewFragment;
    private final SplashRepository repository;

    public SplashPresenter(SplashFragment viewFragment, SplashRepository repository) {
        this.viewFragment = viewFragment;
        this.repository = repository;
    }

    public void isBoardingFinish() {
        viewFragment.isBoardingFinish(repository.isBoardingFinish());
    }

    public void isUserLogin() {
        viewFragment.isUserLogin(repository.isUserLogin());
    }
}
