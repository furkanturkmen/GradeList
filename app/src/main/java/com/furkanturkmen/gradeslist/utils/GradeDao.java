package com.furkanturkmen.gradeslist.utils;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.furkanturkmen.gradeslist.models.Grade;

import java.util.List;

@Dao
public interface GradeDao {

    @Query("SELECT * FROM grade")
    public List<Grade> getAllGrades();

    @Insert
    public void insertGrades(Grade grades);

    @Delete
    public void deleteGrades(Grade grades);

    @Update
    public void updateGrades(Grade grades);
}
