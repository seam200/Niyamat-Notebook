package com.example.niyamatmynotebook;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.niyamatmynotebook.User;

@Database(entities = {User.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "my_app_database";
    private static volatile MyAppDatabase instance;

    public abstract UserDao userDao();

    public static synchronized MyAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyAppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

