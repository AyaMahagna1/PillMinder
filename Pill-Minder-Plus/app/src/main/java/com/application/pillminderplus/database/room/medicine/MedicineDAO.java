package com.application.pillminderplus.database.room.medicine;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.application.pillminderplus.medications.repository.MedicationsPojo;
import com.application.pillminderplus.model.Medicine;

import java.util.List;
//The Data Access Object (DAO) pattern is a structural pattern that allows us to isolate the application/business layer from the persistence layer (usually a relational database but could be any other persistence mechanism) using an abstract API.
//DAO for medicine in the local database (room)
@Dao
public interface MedicineDAO {

    @Query("SELECT * FROM medicine")
    LiveData<List<Medicine>> getAllMedicines();

    @Query("SELECT * FROM medicine WHERE medicine.id=:medID")
    LiveData<Medicine> getMedicine(String medID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicine(Medicine medicine);

    @Delete
    void deleteMedicine(Medicine medicine);

    @Query("SELECT id, name, strength, remainingMedAmount, form FROM medicine where isActivated = 1")
    LiveData<List<MedicationsPojo>> getActiveMeds();

    @Query("SELECT id, name, strength, remainingMedAmount, form FROM medicine where isActivated = 0")
    LiveData<List<MedicationsPojo>> getInactiveMeds();

    @Query("SELECT * FROM medicine WHERE isSync = 0")
    LiveData<List<Medicine>> getAllUnSyncMedicines();
}
