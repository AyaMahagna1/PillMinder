package com.application.pillminderplus.medecinetasks.addingmedicine;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.util.ArrayList;
//Adding success / failed
public interface AddingMedicineDelegate {
    void onSuccess(Medicine medicine, ArrayList<MedicineDose>doses);
    void onFailure();
}
