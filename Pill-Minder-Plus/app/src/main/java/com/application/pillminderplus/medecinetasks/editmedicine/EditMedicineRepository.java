package com.application.pillminderplus.medecinetasks.editmedicine;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.medecinetasks.displaymedicine.DisplayMedicineDelegate;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.RemoteSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class EditMedicineRepository {
    private static EditMedicineRepository instance = null;
    RemoteSource remoteSource;
    LocalSourceMedicine localSourceMedicine;
    LocalSourceMedicineDose localSourceMedicineDose;
    Medicine medicine;
    ArrayList<MedicineDose> doses;

    private EditMedicineRepository(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        this.remoteSource = remoteSource;
        this.localSourceMedicine = localSourceMedicine;
        this.localSourceMedicineDose = localSourceMedicineDose;
    }

    public static EditMedicineRepository getInstance(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        if(instance == null) {
            instance = new EditMedicineRepository(remoteSource, localSourceMedicine, localSourceMedicineDose);
        }
        return instance;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine, DisplayMedicineDelegate networkDelegate) {
        this.medicine = medicine;
        remoteSource.enqueueCall(networkDelegate, medicine.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<MedicineDose> getDoses() {
        doses.sort(new Comparator<MedicineDose>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(MedicineDose m1, MedicineDose m2) {
                if(LocalDateTime.parse(m1.getTime()).isAfter(LocalDateTime.parse(m2.getTime()))) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        return doses;
    }

    public void setDoses(ArrayList<MedicineDose> doses) {
        this.doses = doses;
    }

    public void addDose(MedicineDose dose) {
        doses.add(dose);
    }

    public void updateMedInFirebase(AddingMedicineDelegate networkDelegate) {
        remoteSource.enqueueCall(networkDelegate, medicine, doses);
    }

    public void updateMedicineInRoom() {
        localSourceMedicine.insertMedicine(medicine);
        for(MedicineDose dose: doses)
        localSourceMedicineDose.insertMedicineDose(dose);
    }
}
