package org.unibl.etf.mr.touristbl.util;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.unibl.etf.mr.touristbl.model.Entry;

import java.util.List;

@Dao
public interface EntryDAO {

    @Query("select * from entry where category=:categoryName ")
    List<Entry> getByCategory(String categoryName);

    @Query("select * from entry")
    List<Entry> getAll();

    @Query("select * from entry where favorite=1")
    List<Entry> getAllFavorites();
    @Insert
    void insertAll(Entry... entries);

    @Delete
    void deleteAll(Entry... entry);

    @Update
    void updateAll(Entry... entries);

}
