package com.application.pillminderplus.medecinetasks.displaymedicine;

import com.application.pillminderplus.model.MedicineDose;

import java.util.ArrayList;
//Displaying success/failed from firebase or successes from local database (room)
public interface DisplayMedicineDelegate {
    void onSuccess(ArrayList<MedicineDose> doses);
    void onFailure();
    void onSuccessLocal();
}
