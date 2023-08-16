package com.application.pillminderplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
//Once your work (MedicineWorkManager class ) is defined, it must be scheduled with the WorkManager service in order to run. WorkManager offers a lot of flexibility in how you schedule your work. You can schedule it to run periodically over an interval of time, or you can schedule it to run only one time.
//However you choose to schedule the work, you will always use a WorkRequest. While a Worker defines the unit of work, a WorkRequest (and its subclasses) define how and when it should be run.
public class WorkRequestManager {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createWorkRequest(MedicineDose dose, Medicine medicine, Context context) {
        @SuppressLint("RestrictedApi")
        Data data = new Data.Builder()
                .put("medicine", JSONSerializer.serializeMedicine(medicine))
                .put("dose", JSONSerializer.serializeMedicineDose(dose))
                .build();
        LocalDateTime dateTime = LocalDateTime.parse(dose.getTime());

        OneTimeWorkRequest addMedRequest = new OneTimeWorkRequest.Builder(MedicineWorkManager.class)
                .setInitialDelay(Duration.between(LocalDateTime.now(), dateTime).toMillis(), TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(medicine.getId())
                .addTag(dose.getId())
                .build();
        WorkManager worker = WorkManager.getInstance(context);
        worker.enqueue(addMedRequest);
    }

    public static void removeWork(String tag, Context context) {
        WorkManager worker = WorkManager.getInstance(context);
        worker.cancelAllWorkByTag(tag);

    }
}
