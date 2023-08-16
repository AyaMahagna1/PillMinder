package com.application.pillminderplus.home;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

public interface OnMedicineClickListener {

    void onMedicineClick(Medicine medicine, MedicineDose medicineDose);
}
