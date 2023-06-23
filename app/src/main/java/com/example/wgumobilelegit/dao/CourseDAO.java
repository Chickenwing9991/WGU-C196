package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.Course;

import java.util.List;


@Dao
public interface CourseDAO {
    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();

    @Query("DELETE FROM courses")
    void deleteAllRows();

    @Query("SELECT courseName FROM courses")
    String[] getCoursesArray();

    @Query("SELECT courseID FROM courses WHERE courseName IS :inputName")
    Integer getClassID(String inputName);

    @Insert
    void insertAll(Course... courses);

    @Insert
    Long insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses WHERE termID IS :id")
    List<Course> getAssociatedCourses(int id);

    @Query("DELETE FROM courses WHERE mentorID IS :id")
    void deleteMentorCourses(Integer id);

    @Query("DELETE FROM courses WHERE termID IS :id")
    void deleteTermCourses(Integer id);


}
