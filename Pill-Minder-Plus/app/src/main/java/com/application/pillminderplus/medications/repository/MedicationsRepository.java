package com.application.pillminderplus.medications.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicationsRepository  {
    private Context context;
    MedicationsLocalSource localSource;
    private static MedicationsRepository repo = null;

    private MedicationsRepository(Context context, MedicationsLocalSource localSource) {
        this.context = context;
        this.localSource = localSource;
    }

    public static MedicationsRepository getInstance(Context context, MedicationsLocalSource localSource){
        if(repo == null)
            repo = new MedicationsRepository(context, localSource);
        return repo;
    }

    public LiveData<List<MedicationsPojo>> getActiveMeds() {
        return localSource.getLocalActiveMeds();
    }

    public LiveData<List<MedicationsPojo>> getInactiveMeds() {
        return localSource.getLocalInactiveMeds();
    }
}
