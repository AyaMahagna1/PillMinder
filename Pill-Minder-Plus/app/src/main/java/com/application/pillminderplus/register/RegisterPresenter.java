package com.application.pillminderplus.register;

import android.net.Uri;

import com.application.pillminderplus.model.User;
import com.application.pillminderplus.network.NetworkImageProfileDelegate;
import com.application.pillminderplus.network.NetworkRegisterDelegate;
//Create user in firebase and local database
public class RegisterPresenter implements NetworkImageProfileDelegate, NetworkRegisterDelegate {
    private final RegisterFragment viewFragment;
    private final RegisterRepository repository;
    private String name, email, password, profileImageURI;

    public RegisterPresenter(RegisterFragment viewFragment, RegisterRepository repository) {
        this.viewFragment = viewFragment;
        this.repository = repository;
    }

    public void uploadProfileImage(Uri uriProfileImage) {
        repository.uploadProfileImage(this, uriProfileImage);
    }

    public void createUserOnFirebase(String name, String email, String password, String profileImageURI) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageURI = profileImageURI;
        repository.createUserOnFirebase(this, name, email, password, profileImageURI);
    }

    @Override
    public void onResponseSuccess(String uri) {
        viewFragment.setProfileImageURI(uri);
        viewFragment.hideImgProgressbar();
    }

    @Override
    public void onFailureResult(String error) {
        viewFragment.onImgUploadError(error);
        viewFragment.hideImgProgressbar();
    }

    @Override
    public void onResponse(String userId) {
        repository.insertUserToRoom(new User(userId, name, email, password, profileImageURI, null));
        repository.setUserLogin(true);
        repository.setUserId(userId);
        viewFragment.hideProgressbar();
        viewFragment.navigateToHomeScreen();
    }

    @Override
    public void onFailure(String error) {
        viewFragment.onError(error);
        viewFragment.hideProgressbar();
    }
}
