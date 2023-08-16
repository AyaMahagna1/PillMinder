package com.application.pillminderplus.network;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.util.Map;

public interface NetworkHomeDelegate {

    void onResponse(Map<Medicine, MedicineDose> returnedMedDosMap);
    void onFailure(String error);
}
