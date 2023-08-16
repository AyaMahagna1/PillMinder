package com.application.pillminderplus.network;

public interface NetworkDelegate {
    void onResponse();
    void onFailure(String error);
}
