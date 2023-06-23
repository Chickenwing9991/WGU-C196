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

    @Query("SELECT note FROM notes WHERE course_ID IS :id")
    Note getNoteText(int id);

    @Query("SELECT noteID FROM notes WHERE course_ID IS :id")
    Note getNoteId(int id);

    @Query("DELETE FROM notes")
    void deleteAllRows();

    @Insert
    void insertAll(Note... notes);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
