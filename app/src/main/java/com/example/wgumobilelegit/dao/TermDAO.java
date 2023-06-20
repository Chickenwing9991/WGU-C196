package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.Term;

import java.util.List;


@Dao
public interface TermDAO {
    @Query("SELECT * FROM terms")
    List<Term> getAllTerms();

    @Query("SELECT term_name FROM terms")
    String[] getTermsArray();

    @Query("DELETE FROM terms")
    void deleteAllRows();

    @Insert
    void insertAll(Term... terms);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);
}
