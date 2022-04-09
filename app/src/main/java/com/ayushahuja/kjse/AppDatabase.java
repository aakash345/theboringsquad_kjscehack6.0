package com.ayushahuja.kjse;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NotiClass.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotiDAO userDao();
}
