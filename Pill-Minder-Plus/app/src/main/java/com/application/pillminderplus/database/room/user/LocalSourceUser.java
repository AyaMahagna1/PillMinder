package com.application.pillminderplus.database.room.user;

import android.content.Context;

import com.application.pillminderplus.database.room.ApplicationDataBase;
import com.application.pillminderplus.model.User;
//Singleton
//functions in rooms for user such as get and insert
public class LocalSourceUser {

    private final UserDAO dao;
    private static LocalSourceUser localSourceUser;

    private LocalSourceUser(Context context) {
        ApplicationDataBase applicationDataBase = ApplicationDataBase.getInstance(context.getApplicationContext());
        dao = applicationDataBase.userDAO();
    }

    public static LocalSourceUser getInstance(Context context) {
        if (localSourceUser == null) {
            localSourceUser = new LocalSourceUser(context);
        }

        return localSourceUser;
    }

    public void insertUser(User user) {
        new Thread(() -> dao.insertUser(user)).start();
    }

    public User getUser(String userId) {
        return dao.getUser(userId);
    }
}
