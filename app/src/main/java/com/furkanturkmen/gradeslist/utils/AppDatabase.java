package com.furkanturkmen.gradeslist.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.furkanturkmen.gradeslist.models.Grade;

@Database(entities = {Grade.class}, version = 1)
public abstract  class AppDatabase extends RoomDatabase {

    public abstract GradeDao gradeDao();
    private final static String NAME_DATABASE = "grade_db";

    //Static instance
    private static AppDatabase sInstance;
    public static AppDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, NAME_DATABASE).allowMainThreadQueries().build();
        }
        return sInstance;
    }
}