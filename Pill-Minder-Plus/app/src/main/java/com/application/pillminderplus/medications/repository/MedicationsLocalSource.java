package com.application.pillminderplus.medications.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.application.pillminderplus.database.room.ApplicationDataBase;
import com.application.pillminderplus.database.room.medicine.MedicineDAO;

import java.util.List;
//Medication local database (room)
public class MedicationsLocalSource {

    private static MedicationsLocalSource localSource = null;
    Context context;
    LiveData<List<MedicationsPojo>> activeMeds;
    LiveData<List<MedicationsPojo>> inActiveMeds;
    MedicineDAO medDAO;


    private MedicationsLocalSource(Context context) {
        this.context = context;
        ApplicationDataBase dataBase = ApplicationDataBase.getInstance(context);
        medDAO = dataBase.medicineDAO();
        activeMeds = medDAO.getActiveMeds();
        inActiveMeds = medDAO.getInactiveMeds();
    }

    public static MedicationsLocalSource getInstance(Context context){
        if(localSource == null)
            localSource = new MedicationsLocalSource(context);
        return localSource;
    }

    public LiveData<List<MedicationsPojo>> getLocalActiveMeds(){
        return activeMeds;
    }
    public LiveData<List<MedicationsPojo>> getLocalInactiveMeds(){
        return inActiveMeds;
    }
}
