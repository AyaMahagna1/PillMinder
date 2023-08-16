package com.application.pillminderplus.network;

public interface NetworkRegisterDelegate {
    void onResponse(String userId);
    void onFailure(String error);
}
