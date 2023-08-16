package com.application.pillminderplus.login;

import android.app.Activity;
import android.content.Context;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.database.SharedPreferenceManager;
import com.application.pillminderplus.model.User;
import com.application.pillminderplus.network.NetworkLoginDelegate;
import com.application.pillminderplus.network.RemoteSource;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class LoginRepository {

    RemoteSource remoteSource;
    LocalSourceUser localSourceUser;
    Context context;
    private static LoginRepository loginRepository = null;

    private LoginRepository(RemoteSource remoteSource, LocalSourceUser localSourceUser, Context context) {
        this.remoteSource = remoteSource;
        this.localSourceUser = localSourceUser;
        this.context = context;
    }

    public static LoginRepository getInstance(RemoteSource remoteSource, LocalSourceUser localSourceUser, Context context) {
        if (loginRepository == null) {
            loginRepository = new LoginRepository(remoteSource, localSourceUser, context);
        }

        return loginRepository;
    }

    public void signInWithEmailAndPassword(NetworkLoginDelegate networkDelegate, String email, String password) {
        remoteSource.enqueueCall(networkDelegate, email, password);
    }

    public void setUserLogin(boolean isLogin) {
        SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).setValue(Constants.USER_LOGIN_KEY, isLogin);
    }

    public GoogleSignInClient getGoogleSignInClient(Activity activity) {
        return remoteSource.getGoogleSignInClient(activity);
    }

    public void signInWithGoogle(NetworkLoginDelegate networkDelegate, String idToken) {
        remoteSource.signInWithGoogle(networkDelegate, idToken);
    }

    public void insertUserToRoom(User user) {
        localSourceUser.insertUser(user);
    }

    public void setUserId(String userId) {
        SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).setValue(Constants.USER_ID_KEY, userId);
    }
}
