package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.Course;

import java.util.List;


@Dao
public interface AssessmentDAO {

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Query("DELETE FROM assessments")
    void deleteAllRows();

    @Query("SELECT * FROM assessments WHERE classID IS :id")
    List<Assessment> getAssociatedAssessments(int id);

    @Insert
    void insertAll(Assessment... assessments);

    @Insert
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);
}
