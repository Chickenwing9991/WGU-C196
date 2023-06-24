package com.example.wgumobilelegit.database;

import androidx.room.TypeConverter;

import com.example.wgumobilelegit.Objects.AssessmentType;
import com.example.wgumobilelegit.Objects.CourseStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Converters {

    //Formatter for readability, consistency, data conversion.
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    @TypeConverter
    public static LocalDate fromDateString(String string){
        return LocalDate.parse(string);
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date){
        return formatter.format(date);
    }

    @TypeConverter
    public static String fromAssessmentType(AssessmentType type){
        switch (type){
            case Performance:
                return AssessmentType.Performance.name();
            case Objective:
                return AssessmentType.Objective.name();
            default:
                return null;
        }
    }

    @TypeConverter
    public static AssessmentType fromAssessmentString(String s){
        switch (s.toLowerCase()){
            case "performance":
                return AssessmentType.Performance;
            case "objective":
                return AssessmentType.Objective;
            default:
                return null;
        }
    }

    @TypeConverter
    public static String fromCourseStatus(CourseStatus status) {
        return (status != null) ? status.name() : null;
    }


    @TypeConverter
    public static CourseStatus fromStatusString(String s) {
        if (s == null) {
            return null;
        }

        switch (s) {
            case "InProgress":
                return CourseStatus.InProgress;
            case "Completed":
                return CourseStatus.Completed;
            case "Dropped":
                return CourseStatus.Dropped;
            case "PlanToTake":
                return CourseStatus.PlanToTake;
            default:
                return null;
        }
    }

}
