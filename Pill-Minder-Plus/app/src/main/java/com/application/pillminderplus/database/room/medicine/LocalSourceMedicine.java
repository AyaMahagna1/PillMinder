package com.application.pillminderplus.database.room.medicine;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.application.pillminderplus.database.room.ApplicationDataBase;
import com.application.pillminderplus.model.Medicine;

import java.util.List;
//functions in rooms for medicine such as get, update, delete and insert
public class LocalSourceMedicine {
// Singleton
    private static LocalSourceMedicine localSource = null;
    private MedicineDAO dao;
    private LiveData<List<Medicine>> allUnSyncMedicines;

    private LocalSourceMedicine(Context context) {
        ApplicationDataBase dataBase = ApplicationDataBase.getInstance(context.getApplicationContext());
        dao = dataBase.medicineDAO();
        allUnSyncMedicines = dao.getAllUnSyncMedicines();
    }

    public static LocalSourceMedicine getInstance(Context context) {
        if (localSource == null) {
            localSource = new LocalSourceMedicine(context);
        }
        return localSource;
    }

    public void insertMedicine(Medicine medicine) {
        new Thread(() -> {
            dao.insertMedicine(medicine);
        }).start();
    }

    public void deleteMedicine(Medicine medicine) {
        new Thread(() -> {
            dao.deleteMedicine(medicine);
        }).start();
    }

    public LiveData<Medicine> getMedicine(String medID) {
        return dao.getMedicine(medID);
    }

    public LiveData<List<Medicine>> getAllUnSyncMedicines() {
        return allUnSyncMedicines;
    }

    public void updateMedicinesInRoom(Medicine updatedList) {
        new Thread(() -> {
            dao.insertMedicine(updatedList);
        }).start();
    }
}
