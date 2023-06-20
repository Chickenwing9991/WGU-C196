package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.Assessment;

import java.util.List;


@Dao
public interface AssessmentDAO {

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Query("DELETE FROM assessments")
    void deleteAllRows();

    @Insert
    void insertAll(Assessment... assessments);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);
}
