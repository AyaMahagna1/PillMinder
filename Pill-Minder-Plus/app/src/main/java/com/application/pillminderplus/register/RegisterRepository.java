package com.application.pillminderplus.register;

import android.content.Context;
import android.net.Uri;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.database.SharedPreferenceManager;
import com.application.pillminderplus.model.User;
import com.application.pillminderplus.network.NetworkRegisterDelegate;
import com.application.pillminderplus.network.NetworkImageProfileDelegate;
import com.application.pillminderplus.network.RemoteSource;

public class RegisterRepository {
    RemoteSource remoteSource;
    LocalSourceUser localSourceUser;
    Context context;
    private static RegisterRepository registerRepository = null;

    private RegisterRepository(RemoteSource remoteSource, LocalSourceUser localSourceUser, Context context) {
        this.remoteSource = remoteSource;
        this.localSourceUser = localSourceUser;
        this.context = context;
    }

    public static RegisterRepository getInstance(RemoteSource remoteSource, LocalSourceUser localSourceUser, Context context) {
        if (registerRepository == null) {
            registerRepository = new RegisterRepository(remoteSource, localSourceUser, context);
        }

        return registerRepository;
    }

    public void uploadProfileImage(NetworkImageProfileDelegate networkImageProfileDelegate, Uri uriProfileImage) {
        remoteSource.enqueueCall(networkImageProfileDelegate, uriProfileImage);
    }

    public void createUserOnFirebase(NetworkRegisterDelegate networkRegisterDelegate, String name, String email, String password, String profileImageURI) {
        remoteSource.enqueueCall(networkRegisterDelegate, name, email, password, profileImageURI);
    }

    public void insertUserToRoom(User user) {
        localSourceUser.insertUser(user);
    }

    public void setUserLogin(boolean isLogin) {
        SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).setValue(Constants.USER_LOGIN_KEY, isLogin);
    }

    public void setUserId(String userId) {
        SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).setValue(Constants.USER_ID_KEY, userId);
    }
}
