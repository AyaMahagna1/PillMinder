package com.application.pillminderplus.notifications;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.WorkRequestManager;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.medecinetasks.displaymedicine.DisplayMedicineDelegate;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.FirebaseClient;

import java.util.ArrayList;

public class NotificationDialogPresenter implements  DisplayMedicineDelegate, AddingMedicineDelegate {

    NotificationDialog notificationDialogView;
    NotificationDialogRepository repo;

    public NotificationDialogPresenter(NotificationDialog notificationDialogView, Medicine medicine, MedicineDose dose) {
        this.notificationDialogView = notificationDialogView;
        repo = NotificationDialogRepository.getInstance(
                FirebaseClient.getInstance(),
                LocalSourceMedicine.getInstance(notificationDialogView.getViewContext()),
                LocalSourceMedicineDose.getInstance(notificationDialogView.getViewContext())
        );
        repo.setMedicine(medicine);
        repo.setDose(dose);

        repo.getStoredDoses(notificationDialogView.getViewContext(), this, notificationDialogView.getLifecycleOwner());

    }

    public Medicine getMedicine() {
        return repo.getMedicine();
    }

    public void setMedicine(Medicine medicine) {
        repo.setMedicine(medicine);
    }

    public MedicineDose getDose() {
        return repo.getDose();
    }

    public void setDose(MedicineDose dose) {
        repo.setDose(dose);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteDose() {
        for(int i = 0; i < repo.getDoses().size(); i++) {
            if(repo.getDoses().get(i).getId().equals(repo.getDose().getId())) {
                repo.getDoses().remove(repo.getDose());
            }
        }

        if(isThisTheUpcomingDose()) {
            MedicineDose upcomingDose = getUpcomingDose();
            WorkRequestManager.removeWork(repo.getMedicine().getId(), notificationDialogView.getViewContext());
            WorkRequestManager.createWorkRequest(upcomingDose, repo.getMedicine(), notificationDialogView.getViewContext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void takeDose() {
        if(isThisTheUpcomingDose()) {

            WorkRequestManager.removeWork(repo.getDose().getId(), notificationDialogView.getViewContext());
        }
        repo.getMedicine().setRemainingMedAmount(repo.getMedicine().getRemainingMedAmount() - repo.getDose().getAmount());
        repo.getDose().setStatus(DoseStatus.TAKEN.getStatus());

        if(repo.getMedicine().getRemainingMedAmount() <= repo.getMedicine().getReminderMedAmount()) {
            notificationDialogView.showRefillReminderDialog();
        }

        for(int i = 0; i < repo.getDoses().size(); i++) {
            if(repo.getDoses().get(i).getId().equals(repo.getDose().getId())) {
                repo.getDoses().set(i, repo.getDose());
            }
        }

        repo.updateMedicineAndDose(notificationDialogView.getViewContext(), this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void skipDose() {
        if(isThisTheUpcomingDose()) {
            WorkRequestManager.removeWork(repo.getDose().getId(), notificationDialogView.getViewContext());
        }
        repo.getMedicine().setRemainingMedAmount(repo.getMedicine().getRemainingMedAmount() - repo.getDose().getAmount());
        repo.getDose().setStatus(DoseStatus.SKIPPED.getStatus());

        for(int i = 0; i < repo.getDoses().size(); i++) {
            if(repo.getDoses().get(i).getId().equals(repo.getDose().getId())) {
                repo.getDoses().set(i, repo.getDose());
            }
        }

        repo.updateMedicineAndDose(notificationDialogView.getViewContext(), this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccess(ArrayList<MedicineDose> doses) {
        repo.setDoses(doses);
    }

    @Override
    public void onSuccess(Medicine medicine, ArrayList<MedicineDose> doses) {
        repo.updateMedicineAndDoseLocal(medicine, doses);
    }

    @Override
    public void onFailure() {
        notificationDialogView.showToast("Operation failed");
    }

    @Override
    public void onSuccessLocal() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isThisTheUpcomingDose() {
        MedicineDose upcomingDose = null;
        for(MedicineDose dose: repo.getDoses()) {
            if(dose.getStatus().equals(DoseStatus.FUTURE.getStatus())) {
                upcomingDose = dose;
                break;
            }
        }
        if(upcomingDose == null) {
            return false;
        }
        return upcomingDose.getId().equals(repo.getDose().getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private MedicineDose getUpcomingDose() {
        MedicineDose upcomingDose = null;
        for(MedicineDose dose: repo.getDoses()) {
            if(dose.getStatus().equals(DoseStatus.FUTURE.getStatus())) {
                upcomingDose = dose;
                break;
            }
        }
        return upcomingDose;
    }
}
