package com.application.pillminderplus.database.room.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.application.pillminderplus.model.User;
//The Data Access Object (DAO) pattern is a structural pattern that allows us to isolate the application/business layer from the persistence layer (usually a relational database but could be any other persistence mechanism) using an abstract API.
//DAO for user in the local database (room)
@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE userId LIKE :userId " + "LIMIT 1")
    User getUser(String userId);
}
