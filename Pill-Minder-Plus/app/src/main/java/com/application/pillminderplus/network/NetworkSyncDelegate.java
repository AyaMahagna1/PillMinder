package com.application.pillminderplus.network;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

public interface NetworkSyncDelegate {
    void onResponse(Medicine syncedMedicinesList, boolean isMed);

    void onResponse(MedicineDose syncedMedicineDosesList);

    void onFailure(String error);
}
