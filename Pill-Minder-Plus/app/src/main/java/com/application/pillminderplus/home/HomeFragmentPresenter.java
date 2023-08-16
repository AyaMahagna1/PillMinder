package com.application.pillminderplus.home;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.NetworkHomeDelegate;

import java.util.Date;
import java.util.Map;
public class HomeFragmentPresenter implements  NetworkHomeDelegate {

    HomeFragment viewInterface;
    HomeFragmentRepository repository;

    public HomeFragmentPresenter(HomeFragment viewInterface, HomeFragmentRepository repository) {
        this.viewInterface = viewInterface;
        this.repository = repository;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getAllDosesWithMedicineName(Date currentDate, Context context) {
        viewInterface.setDosesToAdapter(repository.getAllDosesWithMedicineName(currentDate, context));
    }

    public void getAllDosesWithMedicineNameForUser(Date currentDate, String uid) {
        repository.getAllDosesWithMedicineNameForUser(currentDate, uid, this);
    }

    @Override
    public void onResponse(Map<Medicine, MedicineDose> listMap) {
        viewInterface.setDosesToAdapter(listMap);
    }

    @Override
    public void onFailure(String error) {
        viewInterface.onError(error);
    }
}
