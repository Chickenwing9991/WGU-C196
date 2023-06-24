package com.example.wgumobilelegit.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.wgumobilelegit.database.Converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Define the Course entity for the Room database
@Entity (tableName = "courses")
public class Course implements Parcelable {

    // Define the primary key for the Course entity
    @PrimaryKey (autoGenerate = true)
    public Integer courseID;
    @ColumnInfo
    public Integer termID;
    @ColumnInfo
    public Integer mentorID;
    @ColumnInfo
    public String courseName;
    @ColumnInfo
    public LocalDate startDate;
    @ColumnInfo
    public LocalDate endDate;
    @ColumnInfo
    public CourseStatus status;

    // Constructor for the Course entity
    public Course(Integer courseID, Integer termID, Integer mentorID,
                  String courseName, LocalDate startDate, LocalDate endDate, CourseStatus status) {
        this.courseID = courseID;
        this.termID = termID;
        this.mentorID = mentorID;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Overloaded constructors for the Course entity
    @Ignore
    public Course(
                  String courseName, LocalDate startDate, LocalDate endDate, CourseStatus status) {
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    @Ignore
    public Course(int courseID,
            String courseName, LocalDate startDate, LocalDate endDate, CourseStatus status) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    @Ignore
    public Course(int courseID, int mentorID,
                  String courseName, LocalDate startDate, LocalDate endDate) {
        this.courseID = courseID;
        this.mentorID = mentorID;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Course(Integer termID, Integer mentorID,
                  String courseName, LocalDate startDate, LocalDate endDate, CourseStatus status) {
        this.mentorID = mentorID;
        this.termID = termID;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Method to read data from a Parcel
    protected Course(Parcel in) {
        if (in.readByte() == 0) {
            courseID = null;
        } else {
            courseID = in.readInt();
        }
        if (in.readByte() == 0) {
            termID = null;
        } else {
            termID = in.readInt();
        }
        if (in.readByte() == 0) {
            mentorID = null;
        } else {
            mentorID = in.readInt();
        }
        courseName = in.readString();
        startDate = LocalDate.parse(in.readString());
        endDate = LocalDate.parse(in.readString());
        status = Converters.fromStatusString(in.readString());
    }

    // Method to write data to a Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (courseID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(courseID);
        }
        if (termID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(termID);
        }
        if (mentorID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mentorID);
        }
        dest.writeString(courseName);
        dest.writeString(startDate.format(formatter));
        dest.writeString(endDate.format(formatter));
        dest.writeString(Converters.fromCourseStatus(status));
    }

    // Method to describe the contents of the Parcel
    @Override
    public int describeContents() {
        return 0;
    }

    // Creator for the Parcelable interface
    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    // Getter and setter methods for the Course entity
    public Integer getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
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

    public CourseStatus getCourseStatus() {
        return status;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    public Integer getMentorID() {
        return mentorID;
    }
    public void setMentorID(Integer mentorID) {
        this.mentorID = mentorID;
    }

    // Method to return the course name as a string
    @NonNull
    @Override
    public String toString() {
        return this.courseName;
    }
}
