package com.application.pillminderplus.medicinereminder;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
//Getting user update doses or snooze
public class MedicineReminderPresenter {

    private final MedicationReminderActivity viewActivity;
    private final MedicineReminderRepository repository;

    public MedicineReminderPresenter(MedicationReminderActivity viewActivity, MedicineReminderRepository repository) {
        this.viewActivity = viewActivity;
        this.repository = repository;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUserFromRoom(Context context) {
        viewActivity.displayUserInformation(repository.getUserFromRoom(context));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDose(MedicineDose medicineDose, Medicine medicine, Context context) {
        repository.updateDose(medicineDose, medicine, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void snoozeDose(MedicineDose medicineDose, Medicine medicine, Context context) {
        repository.snoozeDose(medicineDose, medicine, context);
    }
}
