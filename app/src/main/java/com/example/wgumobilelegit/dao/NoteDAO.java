package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.Note;

import java.util.List;


@Dao
public interface NoteDAO {

    @Query("SELECT * FROM notes WHERE course_ID IS :id")
    List<Note> getAllNotes(int id);

    @Query("DELETE FROM notes")
    void deleteAllRows();

    @Insert
    void insertAll(Note... notes);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
