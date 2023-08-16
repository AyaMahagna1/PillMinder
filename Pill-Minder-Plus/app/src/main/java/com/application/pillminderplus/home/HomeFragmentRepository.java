package com.application.pillminderplus.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.database.SharedPreferenceManager;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.NetworkHomeDelegate;
import com.application.pillminderplus.network.RemoteSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
//Get all doses to be loaded in the calender
public class HomeFragmentRepository {
    RemoteSource remoteSource;
    LocalSourceMedicineDose localSourceMedicineDose;
    private static HomeFragmentRepository homeFragmentRepository = null;
    Map<Medicine, MedicineDose> returnedMedDosMap;

    private HomeFragmentRepository(LocalSourceMedicineDose localSourceMedicineDose, RemoteSource remoteSource) {
        this.localSourceMedicineDose = localSourceMedicineDose;
        this.remoteSource = remoteSource;
    }

    public static HomeFragmentRepository getInstance(LocalSourceMedicineDose localSourceMedicineDose, RemoteSource remoteSource) {
        if (homeFragmentRepository == null) {
            homeFragmentRepository = new HomeFragmentRepository(localSourceMedicineDose, remoteSource);
        }
        return homeFragmentRepository;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Medicine, MedicineDose> getAllDosesWithMedicineName(Date currentDate, Context context) {
        returnedMedDosMap = new HashMap<>();
        Map<Medicine, List<MedicineDose>> allDosesWithMedicineName = localSourceMedicineDose.getAllDosesWithMedicineName(SharedPreferenceManager.getInstance(context, Constants.USERS_FILE).getStringValue(Constants.USER_ID_KEY));
        for (Map.Entry<Medicine, List<MedicineDose>> entry : allDosesWithMedicineName.entrySet()) {
            Medicine key = entry.getKey();
            List<MedicineDose> value = entry.getValue();
            for (int i=0; i<value.size(); i++) {
                String[] dateTime = value.get(i).getTime().split("T"); // 2022-04-25 T 03:41
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parsedDate = formatter.parse(dateTime[0]);

                    if (Objects.requireNonNull(parsedDate).equals(currentDate)) {
                        returnedMedDosMap.put(key, value.get(i));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnedMedDosMap;
    }

    public void getAllDosesWithMedicineNameForUser(Date currentDate, String uid, NetworkHomeDelegate networkHomeDelegate) {
        remoteSource.getAllDosesWithMedicineNameForUser(currentDate, uid, networkHomeDelegate);
    }
}
