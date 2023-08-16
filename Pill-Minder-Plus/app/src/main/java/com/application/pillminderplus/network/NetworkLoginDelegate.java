package com.application.pillminderplus.network;

import com.application.pillminderplus.model.User;

public interface NetworkLoginDelegate {
    void onResponse(String userId);
    void onResponse(User user);
    void onFailure(String error);
}
