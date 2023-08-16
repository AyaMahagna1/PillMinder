package com.application.pillminderplus.home;

import android.content.Context;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.NetworkSyncDelegate;

import java.util.List;

public class HomePresenter implements NetworkSyncDelegate {

    private final HomeActivity viewInterface;
    private final HomeRepository repository;

    public HomePresenter(HomeActivity viewInterface, HomeRepository repository) {
        this.viewInterface = viewInterface;
        this.repository = repository;
    }

    public void getUserFromRoom(Context context) {
        viewInterface.displayUserInformation(repository.getUserFromRoom(context));
    }

    public void signOut(Context context) {
        repository.signOut(context);
        viewInterface.navigateToLoginScreen();
    }

    public void getAllUnSyncMedicines() {
        viewInterface.syncMedicines(repository.getAllUnSyncMedicines());
    }

    public void getAllUnSyncMedicineDoses() {
        viewInterface.syncMedicineDoses(repository.getAllUnSyncMedicineDoses());
    }

    public void syncMedicineListToFirebase(List<Medicine> unSyncedMedicines) {
        repository.syncMedicineListToFirebase(this, unSyncedMedicines);
    }

    public void syncMedicineDosesListToFirebase(List<MedicineDose> unSyncedMedicineDoses) {
        repository.syncMedicineDosesListToFirebase(this, unSyncedMedicineDoses);
    }

    @Override
    public void onResponse(Medicine syncedMedicinesList, boolean isMed) {
        repository.updateMedicinesInRoom(syncedMedicinesList);
    }

    @Override
    public void onResponse(MedicineDose syncedMedicineDosesList) {
        repository.updateMedicineDosesInRoom(syncedMedicineDosesList);
    }

    @Override
    public void onFailure(String error) {
        viewInterface.showSyncError(error);
    }
}
