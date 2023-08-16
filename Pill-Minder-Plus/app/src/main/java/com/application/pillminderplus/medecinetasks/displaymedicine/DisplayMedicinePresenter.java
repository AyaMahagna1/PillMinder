package com.application.pillminderplus.medecinetasks.displaymedicine;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.application.pillminderplus.JSONSerializer;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.FirebaseClient;
import com.application.pillminderplus.MedicineWorkManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DisplayMedicinePresenter implements DisplayMedicineDelegate, AddingMedicineDelegate, DeleteMedicineDelegate {

    DisplayMedicineFragment displayMedView;
    DisplayMedicineRepository repository;

    public DisplayMedicinePresenter(DisplayMedicineFragment displayMedView) {
        this.displayMedView = displayMedView;

        repository = DisplayMedicineRepository.getInstance(FirebaseClient.getInstance(),
                LocalSourceMedicine.getInstance(displayMedView.getViewContext()),
                LocalSourceMedicineDose.getInstance(displayMedView.getViewContext())
        );
    }

    public Medicine getMedicine() {
        return repository.getMedicine();
    }

    public void setMedicine(Medicine medicine) {
        displayMedView.showProgressBar();
        repository.setMedicine(medicine);
        repository.getDosesFromFirebase(this, medicine.getId());
    }

    public ArrayList<MedicineDose> getDoses() {
        return repository.getDoses();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addOneTimeWorkRequest() {
        MedicineDose upcomingDose = repository.getUpcomingDose();
        LocalDateTime dateTime = LocalDateTime.parse(upcomingDose.getTime());
        @SuppressLint("RestrictedApi") Data data = new Data.Builder()
                .put("medicine", JSONSerializer.serializeMedicine(repository.getMedicine()))
                .put("dose", JSONSerializer.serializeMedicineDose(repository.getUpcomingDose()))
                .build();

        OneTimeWorkRequest addMedRequest = new OneTimeWorkRequest.Builder(MedicineWorkManager.class)
                .setInitialDelay(Duration.between(LocalDateTime.now(), dateTime).toMillis(), TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(repository.getMedicine().getId())
                .build();

        WorkManager worker = WorkManager.getInstance(displayMedView.getViewContext());
        worker.enqueue(addMedRequest);
    }

    public void removeOneTimeWorkRequest() {
        WorkManager worker = WorkManager.getInstance(displayMedView.getViewContext());
        worker.cancelAllWorkByTag(repository.getMedicine().getId());
    }

    public void updateMedicine() {
        repository.updateMedicine(this, displayMedView.getViewContext());
    }

    public void deleteMedicine() {
        repository.deleteMedicine(this, displayMedView.getViewContext());
    }

    public void getStoredMedicineAndDoses(String medID) {
        repository.getStoredMedicineAndDoses(this, displayMedView.getViewContext(), displayMedView.getViewLifecycle(), medID);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(ArrayList<MedicineDose> doses) {
        repository.setDoses(doses);
        displayMedView.refreshView();
        displayMedView.hideProgressBar();
    }

    @Override
    public void onSuccess(Medicine medicine, ArrayList<MedicineDose> doses) {
        repository.updateMedicineLocal(medicine, doses);
    }

    @Override
    public void onSuccess() {
        displayMedView.showToast("Medicine deleted successfully");
        displayMedView.closeView();
    }

    @Override
    public void onFailure() {
        displayMedView.showToast("Operation failed.");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccessLocal() {
        displayMedView.refreshView();
        displayMedView.hideProgressBar();
    }
}