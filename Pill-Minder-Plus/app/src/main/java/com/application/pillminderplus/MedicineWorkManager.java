package com.application.pillminderplus;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.util.Map;
//Work is defined using the Worker class. The doWork() method runs asynchronously on a background thread provided by WorkManager.
//To create some work for WorkManager to run, extend the Worker class and override the doWork() method
//In our app we defined the worker for the medicine and its doses so we can run reminders
public class MedicineWorkManager extends Worker {

    private final Context context;

    public MedicineWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Map<String, Object> map =  getInputData().getKeyValueMap();
        Medicine medicine = JSONSerializer.deserializeMedicine((String) map.get("medicine"));
        MedicineDose dose = JSONSerializer.deserializeMedicineDose((String) map.get("dose"));


        Intent startIntent = new Intent(context, AppService.class);
        startIntent.putExtra(Constants.MEDICINE_KEY, medicine);
        startIntent.putExtra(Constants.DOSE_KEY, dose);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startIntent);
        } else {
            context.startService(startIntent);
        }

        return Result.success();
    }
}
