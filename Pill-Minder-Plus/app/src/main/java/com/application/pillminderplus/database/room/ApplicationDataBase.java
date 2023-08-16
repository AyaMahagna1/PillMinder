package com.application.pillminderplus.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.application.pillminderplus.database.room.medicine.MedicineDAO;
import com.application.pillminderplus.database.room.medicinedose.MedicineDoseDAO;
import com.application.pillminderplus.database.room.user.UserDAO;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.model.User;

@Database(entities = {User.class, Medicine.class, MedicineDose.class}, version = 1)
public abstract class ApplicationDataBase extends RoomDatabase {
    // singleton object
    private static ApplicationDataBase instance = null;

    // only one thread every time access this method
    public static synchronized ApplicationDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ApplicationDataBase.class, "pill_minder_db")
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public abstract UserDAO userDAO();

    public abstract MedicineDAO medicineDAO();

    public abstract MedicineDoseDAO timeSchedulerDAO();
}
