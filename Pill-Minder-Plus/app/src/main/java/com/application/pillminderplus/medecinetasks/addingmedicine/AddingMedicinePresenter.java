package com.application.pillminderplus.medecinetasks.addingmedicine;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.WorkRequestManager;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.FirebaseClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

// Adding medicine and dosses to room and firebase
public class AddingMedicinePresenter implements AddingMedicineDelegate {
    ArrayList<String> units = new ArrayList<String>();
    MedicineDayFrequency dayFrequency;
    Integer daysBetweenDoses;
    int timeFrequency;
    LocalDate startDate;
    LocalDate endDate;
    ArrayList<LocalDateTime> times;
    ArrayList<Integer> amounts;
    ArrayList<WeekDays> days;
    ArrayList<MedicineDose> doses;
    private Medicine medicine;
    private MedicineDose medicineDose;
    private AddMedicineView addMedicineView;
    private AddingMedicineRepository medRepo;

    public AddingMedicinePresenter(AddMedicineView addMedicineView) {
        this.addMedicineView = addMedicineView;
        medicine = new Medicine();
        medRepo = AddingMedicineRepository.getInstance(
                FirebaseClient.getInstance(),
                LocalSourceMedicine.getInstance(addMedicineView.getContext()),
                LocalSourceMedicineDose.getInstance(addMedicineView.getContext())
        );
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public MedicineDose getMedicineDose() {
        return medicineDose;
    }

    public void setMedicineDose(MedicineDose medicineDose) {
        this.medicineDose = medicineDose;
    }

    public void addUnit(String unit) {
        units.add(unit);
    }

    public ArrayList<String> getUnits() {
        return units;
    }

    public MedicineDayFrequency getDayFrequency() {
        return dayFrequency;
    }

    public void setDayFrequency(MedicineDayFrequency dayFrequency) {
        this.dayFrequency = dayFrequency;
        days = new ArrayList<>();
    }

    public void setDaysBetweenDoses(Integer daysBetweenDoses) {
        this.daysBetweenDoses = daysBetweenDoses;
    }

    public int getTimeFrequency() {
        return timeFrequency;
    }

    public void setTimeFrequency(int frequency) {
        timeFrequency = frequency;
        times = new ArrayList<>(timeFrequency);
        for (int i = 0; i < timeFrequency; i++) {
            times.add(null);
        }
        amounts = new ArrayList<>(timeFrequency);
        for (int i = 0; i < timeFrequency; i++) {
            amounts.add(null);
        }
    }

    public void putTime(int index, LocalDateTime time) {
        times.set(index, time);
    }

    public void setAmounts(ArrayList<Integer> amounts) {
        this.amounts = amounts;
    }

    public ArrayList<LocalDateTime> getTimes() {
        return times;
    }

    public void setDays(ArrayList<WeekDays> days) {
        this.days = days;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Adding the medicine amd its doses to firebase
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addMedFinished() {

        addMedicineView.showProgressDialog();
        medicine.setActivated(true);
        medicine.setSync(true);

        createSchedule();

        medRepo.insertMedicineInFirebase(this, medicine, doses);
    }

    // Scheduling the doses
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createSchedule() {
        doses = new ArrayList<>();
        if (dayFrequency == MedicineDayFrequency.EVERYDAY) {
            createEverydaySchedule();
        } else if (dayFrequency == MedicineDayFrequency.EVERY_NUMBER_OF_DAYS) {
            createEveryNDaySchedule();
        } else {
            createSpecificDaysSchedule();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createEverydaySchedule() {
        boolean isFirstLoop = true;
        while (!startDate.isAfter(endDate)) {

            for (int i = 0; i < timeFrequency; i++) {
                if (!isFirstLoop || times.get(i).isAfter(LocalDateTime.now())) {
                    addDose(i);
                }
            }
            isFirstLoop = false;
            startDate = startDate.plusDays(1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createEveryNDaySchedule() {
        boolean isFirstLoop = true;
        while (!startDate.isAfter(endDate)) {

            for (int i = 0; i < timeFrequency; i++) {
                if (!isFirstLoop || times.get(i).isAfter(LocalDateTime.now())) {
                    addDose(i);
                }
            }
            isFirstLoop = false;
            startDate = startDate.plusDays(daysBetweenDoses);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createSpecificDaysSchedule() {
        boolean isFirstLoop = true;
        while (!startDate.isAfter(endDate)) {
            if (isSelectedDay()) {
                for (int i = 0; i < timeFrequency; i++) {
                    if (!isFirstLoop || times.get(i).isAfter(LocalDateTime.now())) {
                        addDose(i);
                    }
                }
            }
            isFirstLoop = false;
            startDate = startDate.plusDays(1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDose(int i) {
        MedicineDose dose = new MedicineDose();
        LocalDateTime time = LocalDateTime.of(startDate, times.get(i).toLocalTime());
        dose.setTime(time.toString());
        dose.setAmount(amounts.get(i));
        dose.setStatus(DoseStatus.FUTURE.getStatus());
        dose.setSync(true);
        doses.add(dose);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isSelectedDay() {
        if (days.stream().anyMatch(day -> day.getDay().equals(startDate.getDayOfWeek().toString().toLowerCase(Locale.ROOT)))) {
            return true;
        }
        return false;
    }

    // Listener for adding medicine to firebase if succeeded it'll be added to rooms too
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(Medicine medicine, ArrayList<MedicineDose> doses) {
        medRepo.insertMedicineInRoom(medicine, doses);
        createWorkRequest();
        addMedicineView.hideProgressDialog();
        addMedicineView.showToast("Medicine Was Added Successfully");
        addMedicineView.closeActivity();
    }

    // Failing in adding the medicine to firebase
    @Override
    public void onFailure() {
        addMedicineView.showToast("Can't Add This Medicine");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createWorkRequest() {
        LocalDateTime dateTime = LocalDateTime.parse(doses.get(0).getTime());
        WorkRequestManager.createWorkRequest(doses.get(0), medicine, addMedicineView.getContext());
    }

}
