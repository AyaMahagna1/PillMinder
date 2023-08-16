package com.application.pillminderplus.medecinetasks.addingmedicine;

import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.RemoteSource;

import java.util.ArrayList;

// Singleton class/repository
public class AddingMedicineRepository {

    private static AddingMedicineRepository instance = null;
    LocalSourceMedicine localSourceMedicine;
    LocalSourceMedicineDose localSourceMedicineDose;
    RemoteSource remoteSource;

    private AddingMedicineRepository(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        this.remoteSource = remoteSource;
        this.localSourceMedicine = localSourceMedicine;
        this.localSourceMedicineDose = localSourceMedicineDose;
    }

    public static AddingMedicineRepository getInstance(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        if (instance == null) {
            instance = new AddingMedicineRepository(remoteSource, localSourceMedicine, localSourceMedicineDose);
        }
        return instance;
    }

    // Adding med to rooms
    public void insertMedicineInRoom(Medicine medicine, ArrayList<MedicineDose> doses) {
        medicine.setSync(true);
        localSourceMedicine.insertMedicine(medicine);
        for (MedicineDose medicineDose : doses) {
            medicineDose.setSync(true);
            localSourceMedicineDose.insertMedicineDose(medicineDose);
        }
    }
    // Adding med to firebase
    public void insertMedicineInFirebase(AddingMedicineDelegate networkDelegate, Medicine medicine, ArrayList<MedicineDose> doses) {
        remoteSource.enqueueCall(networkDelegate, medicine, doses);
    }
}
