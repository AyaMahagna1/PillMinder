package com.application.pillminderplus.forgotpassword;

import com.application.pillminderplus.network.NetworkDelegate;
import com.application.pillminderplus.network.RemoteSource;

public class ForgotPasswordRepository {

    RemoteSource remoteSource;
    private static ForgotPasswordRepository forgotPasswordRepository = null;

    private ForgotPasswordRepository(RemoteSource remoteSource) {
        this.remoteSource = remoteSource;
    }

    public static ForgotPasswordRepository getInstance(RemoteSource remoteSource) {
        if (forgotPasswordRepository == null) {
            forgotPasswordRepository = new ForgotPasswordRepository(remoteSource);
        }

        return forgotPasswordRepository;
    }

    public void sendPasswordResetEmail(NetworkDelegate networkDelegate, String email) {
        remoteSource.enqueueCall(networkDelegate, email);
    }
}
