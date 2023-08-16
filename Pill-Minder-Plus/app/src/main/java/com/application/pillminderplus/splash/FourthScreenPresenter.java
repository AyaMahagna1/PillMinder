package com.application.pillminderplus.splash;

import com.application.pillminderplus.splash.screens.FourthScreenFragment;

public class FourthScreenPresenter {

    private FourthScreenFragment viewFragment;
    private FourthScreenRepository repository;

    public FourthScreenPresenter(FourthScreenFragment viewFragment, FourthScreenRepository repository) {
        this.viewFragment = viewFragment;
        this.repository = repository;
    }

    public void setBoardingFinish(boolean isFinish) {
        repository.setBoardingFinish(isFinish);
        viewFragment.setBoardingFinishComplete();
    }
}
