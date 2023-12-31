package com.application.pillminderplus.database.room.medicinedose;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.util.List;
import java.util.Map;
//The Data Access Object (DAO) pattern is a structural pattern that allows us to isolate the application/business layer from the persistence layer (usually a relational database but could be any other persistence mechanism) using an abstract API.
//DAO for medicine doses in the local database (room)
@Dao
public interface MedicineDoseDAO {
    @Query("SELECT * FROM medicine_dose WHERE medID=:medID")
    LiveData<List<MedicineDose>> getAllMedicineDoses(String medID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicineDose(MedicineDose medicineDose);

    @Delete
    void deleteMedicineDose(MedicineDose medicineDose);

    @Update
    void updateMedicineDose(MedicineDose medicineDose);

    @Update
    void updateMedicine(Medicine medicine);

    @Query("SELECT * FROM medicine_dose WHERE status = :futureStatus ORDER BY time LIMIT 1")
    MedicineDose getNextMedicineDose(String futureStatus);

    @Query("SELECT * FROM medicine WHERE id = :med_id LIMIT 1")
    Medicine getNextMedicine(String med_id);


    @Query("SELECT DISTINCT * FROM medicine INNER JOIN medicine_dose ON medicine.id = medicine_dose.medID AND medicine.isActivated = 1 AND :uid = userID")
    Map<Medicine, List<MedicineDose>> getAllDosesWithMedicineName(String uid);

    @Query("SELECT * FROM medicine_dose WHERE isSync = 0")
    LiveData<List<MedicineDose>> getAllUnSyncMedicineDoses();
}
