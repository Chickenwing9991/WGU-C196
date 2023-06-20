package com.example.wgumobilelegit.Objects;

import java.time.LocalDate;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "terms")
public class Term {

    @PrimaryKey (autoGenerate = true)
    public Integer termID;
    @ColumnInfo (name = "term_name")
    public String termName;
    @ColumnInfo (name = "start_date")
    public LocalDate startDate;
    @ColumnInfo (name = "end_date")
    public LocalDate endDate;

    @Ignore
    public Term(Integer termID, String termName, LocalDate startDate, LocalDate endDate) {
        this.termID = termID;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term(String termName, LocalDate startDate, LocalDate endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getTermID() {
        return termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
