package com.application.pillminderplus.medecinetasks.displaymedicine;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.application.pillminderplus.NetworkConnection;
import com.application.pillminderplus.WorkRequestManager;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.RemoteSource;

import java.util.ArrayList;
//Singleton
public class DisplayMedicineRepository {

    private static DisplayMedicineRepository instance = null;
    RemoteSource remoteSource;
    LocalSourceMedicine localSourceMedicine;
    LocalSourceMedicineDose localSourceMedicineDose;
    Medicine medicine;
    ArrayList<MedicineDose> doses;

    private DisplayMedicineRepository(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        this.remoteSource = remoteSource;
        this.localSourceMedicine = localSourceMedicine;
        this.localSourceMedicineDose = localSourceMedicineDose;
        doses = new ArrayList<>();
    }

    public static DisplayMedicineRepository getInstance(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        if (instance == null) {
            instance = new DisplayMedicineRepository(remoteSource, localSourceMedicine, localSourceMedicineDose);
        }
        return instance;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public ArrayList<MedicineDose> getDoses() {
        return doses;
    }

    public void setDoses(ArrayList<MedicineDose> doses) {
        this.doses = doses;
    }

    public void getDosesFromFirebase(DisplayMedicineDelegate networkDelegate, String medID) {
        remoteSource.enqueueCall(networkDelegate, medID);
    }

    public MedicineDose getUpcomingDose() {
        for (MedicineDose dose : doses) {
            if (dose.getStatus().equals(DoseStatus.FUTURE.getStatus())) {
                return dose;
            }
        }
        return null;
    }

    public void updateMedicine(AddingMedicineDelegate networkDelegate, Context context) {
        if (NetworkConnection.isNetworkAvailable(context)) {
            remoteSource.enqueueCall(networkDelegate, medicine, doses);
        } else {
            updateMedicineLocal(medicine, doses);
        }
    }

    public void updateMedicineLocal(Medicine medicine, ArrayList<MedicineDose> doses) {
        localSourceMedicine.insertMedicine(medicine);
        for (MedicineDose dose : doses) {
            localSourceMedicineDose.insertMedicineDose(dose);
        }
    }

    public void getStoredDoses(DisplayMedicineDelegate networkDelegate, Context context, LifecycleOwner owner) {
        if (NetworkConnection.isNetworkAvailable(context)) {
            remoteSource.enqueueCall(networkDelegate, medicine.getId());
        } else {
            localSourceMedicineDose.getAllMedicineDoses(medicine.getId()).observe(owner, storedDoses -> {
                doses = ((ArrayList<MedicineDose>) storedDoses);
                networkDelegate.onSuccessLocal();
            });
        }
    }

    public void deleteMedicine(DeleteMedicineDelegate networkDelegate, Context context) {
        if (NetworkConnection.isNetworkAvailable(context)) {
            remoteSource.enqueueCall(networkDelegate, medicine, doses);

            localSourceMedicine.deleteMedicine(medicine);
            for (MedicineDose dose : doses) {
                localSourceMedicineDose.deleteMedicineDose(dose);
            }
        } else {
            localSourceMedicine.deleteMedicine(medicine);
            for (MedicineDose dose : doses) {
                localSourceMedicineDose.deleteMedicineDose(dose);
            }
        }

        WorkRequestManager.removeWork(medicine.getId(), context);
    }

    public void getStoredMedicineAndDoses(DisplayMedicineDelegate networkDelegate, Context context, LifecycleOwner owner, String medID) {
        localSourceMedicine.getMedicine(medID).observe(owner, med -> {
            medicine = med;
            if (med != null) {
                getStoredDoses(networkDelegate, context, owner);
            }
        });
    }
}
