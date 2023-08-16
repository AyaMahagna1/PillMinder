package com.application.pillminderplus.medecinetasks.editmedicine;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.NetworkConnection;
import com.application.pillminderplus.WorkRequestManager;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.medecinetasks.displaymedicine.DisplayMedicineDelegate;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.FirebaseClient;

import java.util.ArrayList;
//Getting and setting medicine details
public class EditMedicinePresenter implements AddingMedicineDelegate, DisplayMedicineDelegate {
    EditMedicineActivity editMedView;
    EditMedicineRepository repository;

    public EditMedicinePresenter(EditMedicineActivity editMedView) {
        this.editMedView = editMedView;
        repository = EditMedicineRepository.getInstance(
                FirebaseClient.getInstance(),
                LocalSourceMedicine.getInstance(editMedView.getViewContext()),
                LocalSourceMedicineDose.getInstance(editMedView.getViewContext())
        );
    }

    public Medicine getMedicine() {
        return repository.getMedicine();
    }

    public void setMedicine(Medicine medicine) {
        repository.setMedicine(medicine, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<MedicineDose> getDoses() {
        return repository.getDoses();
    }

    public void setDoses(ArrayList<MedicineDose> doses) {
        repository.setDoses(doses);
    }

    public void addDose(MedicineDose dose) {
        repository.addDose(dose);
    }

    public void submitUpdates() {
        if (NetworkConnection.isNetworkAvailable(editMedView.getViewContext())) {
            editMedView.showProgressDialog();
            repository.updateMedInFirebase(this);
        } else {
            repository.updateMedicineInRoom();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(Medicine medicine, ArrayList<MedicineDose> doses) {
        repository.setMedicine(medicine, this);
        repository.setDoses(doses);
        repository.updateMedicineInRoom();
        editMedView.showSuccessToast();
        WorkRequestManager.removeWork(getMedicine().getId(), editMedView.getViewContext());


        MedicineDose upcomingDose = null;
        for (MedicineDose dose : getDoses()) {
            if (dose.getStatus().equals(DoseStatus.FUTURE.getStatus())) {
                upcomingDose = dose;
                break;
            }
        }

        WorkRequestManager.createWorkRequest(
                upcomingDose,
                getMedicine(),
                editMedView.getViewContext()
        );

        editMedView.hideProgressDialog();
        editMedView.closeView();

    }

    @Override
    public void onSuccess(ArrayList<MedicineDose> doses) {
        repository.setDoses(doses);
        editMedView.hideProgressDialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editMedView.setUI();
        }
    }

    @Override
    public void onFailure() {
        editMedView.showFailureToast();
    }

    @Override
    public void onSuccessLocal() {
        editMedView.hideProgressDialog();
    }
}
