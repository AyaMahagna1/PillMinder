package com.application.pillminderplus.notifications;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.application.pillminderplus.NetworkConnection;
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
import java.util.List;
//Get and set doses depending on notifications
public class NotificationDialogRepository {
    Medicine medicine;
    MedicineDose dose;

    ArrayList<MedicineDose> doses;

    private static NotificationDialogRepository instance = null;

    RemoteSource remoteSource;
    LocalSourceMedicine localSourceMedicine;
    LocalSourceMedicineDose localSourceMedicineDose;

    private NotificationDialogRepository(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        this.remoteSource = remoteSource;
        this.localSourceMedicine = localSourceMedicine;
        this.localSourceMedicineDose = localSourceMedicineDose;
    }

    public static NotificationDialogRepository getInstance(RemoteSource remoteSource, LocalSourceMedicine localSourceMedicine, LocalSourceMedicineDose localSourceMedicineDose) {
        if(instance == null) {
            instance = new NotificationDialogRepository(remoteSource, localSourceMedicine, localSourceMedicineDose);
        }
        return instance;
    }


    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public MedicineDose getDose() {
        return dose;
    }

    public void setDose(MedicineDose dose) {
        this.dose = dose;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDoses(ArrayList<MedicineDose> doses) {
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
        this.doses = doses;
    }

    public void getStoredDoses(Context context, DisplayMedicineDelegate networkDelegate, LifecycleOwner owner) {
        if(NetworkConnection.isNetworkAvailable(context)) {
            remoteSource.enqueueCall(networkDelegate, medicine.getId());
        }
        else {
            localSourceMedicineDose.getAllMedicineDoses(medicine.getId()).observe(owner, new Observer<List<MedicineDose>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChanged(List<MedicineDose> medicineDoses) {
                    setDoses((ArrayList<MedicineDose>) medicineDoses);
                }
            });
        }
    }

    public void deleteDose() {

    }

    public void updateMedicineAndDose(Context context, AddingMedicineDelegate networkDelegate) {
        if(NetworkConnection.isNetworkAvailable(context)) {
            remoteSource.enqueueCall(networkDelegate, medicine, doses);
        }
        else {
            updateMedicineAndDoseLocal(medicine, doses);
        }

    }

    public void updateMedicineAndDoseLocal(Medicine medicine, ArrayList<MedicineDose> doses) {
        localSourceMedicine.insertMedicine(medicine);
        for(MedicineDose dose: doses) {
            localSourceMedicineDose.insertMedicineDose(dose);
        }
    }

    public void skipDose() {

    }
}
