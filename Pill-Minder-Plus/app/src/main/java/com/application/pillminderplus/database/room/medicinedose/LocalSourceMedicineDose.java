package com.application.pillminderplus.database.room.medicinedose;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.application.pillminderplus.database.room.ApplicationDataBase;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.util.List;
import java.util.Map;
//functions in rooms for medicine doses such as get, update, delete and insert
public class LocalSourceMedicineDose  {

    private static LocalSourceMedicineDose instance = null;
    private final MedicineDoseDAO dao;
    private LiveData<List<MedicineDose>> storedMedicineDosesLiveData;

    private LocalSourceMedicineDose(Context context) {
        ApplicationDataBase database = ApplicationDataBase.getInstance(context.getApplicationContext());
        dao = database.timeSchedulerDAO();
    }

    public static LocalSourceMedicineDose getInstance(Context context) {
        if(instance == null) {
            instance = new LocalSourceMedicineDose(context.getApplicationContext());
        }
        return instance;
    }
    public LiveData<List<MedicineDose>> getAllMedicineDoses(String medID) {
        storedMedicineDosesLiveData = dao.getAllMedicineDoses(medID);
        return storedMedicineDosesLiveData;
    }

    public void insertMedicineDose(MedicineDose medicineDose) {
        new Thread() {
            @Override
            public void run() {
                dao.insertMedicineDose(medicineDose);
            }
        }.start();
    }

    public void deleteMedicineDose(MedicineDose medicineDose) {
        new Thread() {
            @Override
            public void run() {
                dao.deleteMedicineDose(medicineDose);
            }
        }.start();
    }

    public void updateMedicineDose(MedicineDose medicineDose) {
        dao.updateMedicineDose(medicineDose);
    }

    public void updateMedicine(Medicine medicine) {
        dao.updateMedicine(medicine);
    }

    public MedicineDose getNextMedicineDose() {
        return dao.getNextMedicineDose(DoseStatus.FUTURE.getStatus());
    }

    public Medicine getNextMedicine(String med_id) {
        return dao.getNextMedicine(med_id);
    }

    public Map<Medicine, List<MedicineDose>> getAllDosesWithMedicineName(String uid) {
        return dao.getAllDosesWithMedicineName(uid);
    }

    public LiveData<List<MedicineDose>> getAllUnSyncMedicineDoses() {
        return dao.getAllUnSyncMedicineDoses();
    }

    public void updateMedicineDosesInRoom(MedicineDose updatedList) {
        new Thread(() -> {
            dao.updateMedicineDose(updatedList);
        }).start();
    }
}
