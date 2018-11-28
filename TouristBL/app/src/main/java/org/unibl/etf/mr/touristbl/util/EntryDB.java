package org.unibl.etf.mr.touristbl.util;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.unibl.etf.mr.touristbl.model.Entry;
import org.unibl.etf.mr.touristbl.model.NewsDetails;
import org.unibl.etf.mr.touristbl.util.EntryDAO;

@Database(entities = {Entry.class}, version = 4)

public abstract class EntryDB extends RoomDatabase{


    public abstract EntryDAO entryDAO();
}
