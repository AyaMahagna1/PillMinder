package com.application.pillminderplus.splash;

import android.content.Context;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.database.SharedPreferenceManager;

public class FourthScreenRepository {

    Context context;
    private static FourthScreenRepository fourthScreenRepository = null;

    private FourthScreenRepository(Context context) {
        this.context = context;
    }

    public static FourthScreenRepository getInstance(Context context) {
        if (fourthScreenRepository == null) {
            fourthScreenRepository = new FourthScreenRepository(context);
        }

        return fourthScreenRepository;
    }

    public void setBoardingFinish(boolean isFinish) {
        SharedPreferenceManager.getInstance(context, Constants.ON_BOARDING_FILE).setValue(Constants.ON_BOARDING_FINISH_KEY, isFinish);
    }
}
