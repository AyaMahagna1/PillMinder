package com.application.pillminderplus.medications;

import androidx.lifecycle.LiveData;

import com.application.pillminderplus.medications.repository.MedicationsRepository;
import com.application.pillminderplus.medications.repository.MedicationsPojo;

import java.util.List;

public class MedicationsPresenter {

    MedicationsFragment view;
    MedicationsRepository repo;

    public MedicationsPresenter(MedicationsFragment view, MedicationsRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    public LiveData<List<MedicationsPojo>> getActiveMeds() {
        return repo.getActiveMeds();
    }

    public LiveData<List<MedicationsPojo>> getInactiveMeds() {
        return repo.getInactiveMeds();
    }
}
