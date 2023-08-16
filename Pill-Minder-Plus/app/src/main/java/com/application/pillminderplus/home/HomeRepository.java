package com.application.pillminderplus.home;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.database.SharedPreferenceManager;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.model.User;
import com.application.pillminderplus.network.NetworkSyncDelegate;
import com.application.pillminderplus.network.RemoteSource;

import java.util.List;
//Getting and updating doses in firebase and local database (room)
public class HomeRepository {

    RemoteSource remoteSource;
    LocalSourceUser localSourceUser;
    LocalSourceMedicine localSourceMedicine;
    LocalSourceMedicineDose localSourceMedicineDose;
    private static HomeRepository homeRepository = null;

    private HomeRepository(LocalSourceUser localSourceUser, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose, RemoteSource remoteSource) {
        this.localSourceUser = localSourceUser;
        this.remoteSource = remoteSource;
        this.localSourceMedicine = localSourceMedicine;
        this.localSourceMedicineDose = localSourceMedicineDose;
    }

    public static HomeRepository getInstance(LocalSourceUser localSourceUser, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose, RemoteSource remoteSource) {
        if (homeRepository == null) {
            homeRepository = new HomeRepository(localSourceUser, localSourceMedicine, localSourceMedicineDose, remoteSource);
        }

        return homeRepository;
    }

    public User getUserFromRoom(Context context) {
        String userId = SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).getStringValue(Constants.USER_ID_KEY);
        return localSourceUser.getUser(userId);
    }

    public void signOut(Context context) {
        remoteSource.signOut();
        SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).setValue(Constants.USER_LOGIN_KEY, false);
    }

    public LiveData<List<Medicine>> getAllUnSyncMedicines() {
        return localSourceMedicine.getAllUnSyncMedicines();
    }

    public LiveData<List<MedicineDose>> getAllUnSyncMedicineDoses() {
        return localSourceMedicineDose.getAllUnSyncMedicineDoses();
    }

    public void syncMedicineListToFirebase(NetworkSyncDelegate networkDelegate, List<Medicine> unSyncedMedicines) {
        remoteSource.syncMedicineListToFirebase(networkDelegate, unSyncedMedicines);
    }

    public void syncMedicineDosesListToFirebase(NetworkSyncDelegate networkDelegate, List<MedicineDose> unSyncedMedicineDoses) {
        remoteSource.syncMedicineDosesListToFirebase(networkDelegate, unSyncedMedicineDoses);
    }

    public void updateMedicinesInRoom(Medicine updatedList) {
        localSourceMedicine.updateMedicinesInRoom(updatedList);
    }

    public void updateMedicineDosesInRoom(MedicineDose updatedList) {
        localSourceMedicineDose.updateMedicineDosesInRoom(updatedList);
    }
}
