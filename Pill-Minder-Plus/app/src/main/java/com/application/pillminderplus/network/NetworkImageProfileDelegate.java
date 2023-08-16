package com.application.pillminderplus.network;

public interface NetworkImageProfileDelegate {
    void onResponseSuccess(String uri);
    void onFailureResult(String error);
}
