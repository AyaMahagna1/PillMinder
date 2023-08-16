package com.application.pillminderplus.splash;

import android.content.Context;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.database.SharedPreferenceManager;

public class SplashRepository {

    Context context;
    private static SplashRepository splashRepository = null;

    private SplashRepository(Context context) {
        this.context = context;
    }

    public static SplashRepository getInstance(Context context) {
        if (splashRepository == null) {
            splashRepository = new SplashRepository(context);
        }

        return splashRepository;
    }

    public boolean isBoardingFinish() {
        return SharedPreferenceManager.getInstance(context, Constants.ON_BOARDING_FILE).getBooleanValue(Constants.ON_BOARDING_FINISH_KEY);
    }

    public boolean isUserLogin() {
        return SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).getBooleanValue(Constants.USER_LOGIN_KEY);
    }
}
