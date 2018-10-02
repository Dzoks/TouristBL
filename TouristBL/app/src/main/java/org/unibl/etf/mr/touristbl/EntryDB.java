package org.unibl.etf.mr.touristbl;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import java.io.Serializable;

@Database(entities = {Entry.class}, version = 1)

public abstract class EntryDB extends RoomDatabase{

    public abstract EntryDAO entryDAO();
}
