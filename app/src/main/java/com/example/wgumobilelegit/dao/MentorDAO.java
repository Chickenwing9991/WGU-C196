package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.Mentor;

import java.util.List;


@Dao
public interface MentorDAO {

    @Query("SELECT * FROM mentors")
    List<Mentor> getAllMentors();

    @Query("DELETE FROM mentors")
    void deleteAllRows();

    @Query("SELECT name FROM mentors")
    String[] getMentorsArray();

    @Insert
    void insertAll(Mentor... mentors);

    @Update
    void update(Mentor mentor);

    @Delete
    void delete(Mentor mentor);
}
