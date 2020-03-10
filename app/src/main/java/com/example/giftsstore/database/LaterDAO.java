package com.example.giftsstore.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface LaterDAO {

    @Query("SELECT * FROM Later_on")
    LiveData<List<Gift>> loadLater();

    @Insert
    void insertGift(Gift e);

    @Delete
    void deleteGift(Gift e);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGift(Gift e);

    @Query("SELECT * FROM Later_on WHERE name = :id")
    LiveData<Gift> loadgiftById(int id);
}