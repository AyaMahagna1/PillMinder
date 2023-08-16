package com.application.pillminderplus.medicinereminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.JSONSerializer;
import com.application.pillminderplus.WorkRequestManager;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.database.SharedPreferenceManager;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.model.User;
import com.application.pillminderplus.MedicineWorkManager;

import java.util.concurrent.TimeUnit;

public class MedicineReminderRepository {

    private static MedicineReminderRepository medicineReminderRepository = null;
    LocalSourceUser localSourceUser;
    LocalSourceMedicineDose localSourceMedicineDose;

    private MedicineReminderRepository(LocalSourceUser localSourceUser, LocalSourceMedicineDose localSourceMedicineDose) {
        this.localSourceUser = localSourceUser;
        this.localSourceMedicineDose = localSourceMedicineDose;
    }

    public static MedicineReminderRepository getInstance(LocalSourceUser localSourceUser, LocalSourceMedicineDose localSourceMedicineDose) {
        if (medicineReminderRepository == null) {
            medicineReminderRepository = new MedicineReminderRepository(localSourceUser, localSourceMedicineDose);
        }

        return medicineReminderRepository;
    }

    public User getUserFromRoom(Context context) {
        String userId = SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).getStringValue(Constants.USER_ID_KEY);

        return localSourceUser.getUser(userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDose(MedicineDose medicineDose, Medicine medicine, Context context) {
        localSourceMedicineDose.updateMedicineDose(medicineDose);
        localSourceMedicineDose.updateMedicine(medicine);
        MedicineDose nextMedicineDose = localSourceMedicineDose.getNextMedicineDose();
        Medicine nextMedicine = localSourceMedicineDose.getNextMedicine(nextMedicineDose.getMedID());
        WorkRequestManager.createWorkRequest(nextMedicineDose, nextMedicine, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void snoozeDose(MedicineDose medicineDose, Medicine medicine, Context context) {
        String[] dateTime = medicineDose.getTime().split("T");
        String[] hoursMinutes = dateTime[1].split(":");
        int minutes = Integer.parseInt(hoursMinutes[1]) + 5;
        if (minutes >= 60) {
            hoursMinutes[0] += minutes % 60;
            minutes /= 60;
        }

        medicineDose.setTime(dateTime[0] + "T" + hoursMinutes[0] + ":" + minutes);
        localSourceMedicineDose.updateMedicineDose(medicineDose);

        @SuppressLint("RestrictedApi")
        Data data = new Data.Builder()
                .put("medicine", JSONSerializer.serializeMedicine(medicine))
                .put("dose", JSONSerializer.serializeMedicineDose(medicineDose))
                .build();

        OneTimeWorkRequest addMedRequest = new OneTimeWorkRequest.Builder(MedicineWorkManager.class)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag(medicine.getId())
                .addTag(medicineDose.getId())
                .build();

        WorkManager worker = WorkManager.getInstance(context);
        worker.enqueue(addMedRequest);
    }
}
