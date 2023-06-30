package com.example.wgumobilelegit.Objects;

import static com.example.wgumobilelegit.database.Converters.fromAssessmentString;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Entity class for assessments
@Entity(tableName = "assessments")
public class Assessment implements Parcelable {

    // Primary key for the assessment
    @PrimaryKey(autoGenerate = true)
    public Integer assID;
    // Foreign key for the class
    @ColumnInfo
    public Integer classID;
    // Title of the assessment
    @ColumnInfo
    public String assTitle;
    // Type of the assessment
    @ColumnInfo
    public AssessmentType assType;
    // Due date of the assessment
    @ColumnInfo
    public LocalDate dueDate;

    @ColumnInfo
    public LocalDate startDate;

    // Constructor with all parameters
    @Ignore
    public Assessment(Integer assID, Integer classID, String assTitle, AssessmentType assType,
                       LocalDate dueDate, LocalDate startDate) {
        this.assID = assID;
        this.classID = classID;
        this.assTitle = assTitle;
        this.assType = assType;
        this.dueDate = dueDate;
        this.startDate = startDate;
    }

    // Constructor without assID
    public Assessment(Integer classID, String assTitle, AssessmentType assType,
                      LocalDate dueDate, LocalDate startDate) {
        this.classID = classID;
        this.assTitle = assTitle;
        this.assType = assType;
        this.dueDate = dueDate;
        this.startDate = startDate;
    }

    // Constructor without assID and classID
    @Ignore
    public Assessment(String assTitle, AssessmentType assType,
                      LocalDate dueDate, LocalDate startDate) {
        this.assTitle = assTitle;
        this.assType = assType;
        this.dueDate = dueDate;
        this.startDate = startDate;
    }

    // Constructor to create object from Parcel
    protected Assessment(Parcel in) {

        if (in.readByte() == 0) {
            assID = null;
        } else {
            assID = in.readInt();
        }
        if (in.readByte() == 0) {
            classID = null;
        } else {
            classID = in.readInt();
        }
        // Convert non-parcelable parameters into the object
        dueDate = LocalDate.parse(in.readString());
        assType = fromAssessmentString(in.readString());
        assTitle = in.readString();
    }

    // Creator to create new instances of the Parcelable class
    public static final Creator<Assessment> CREATOR = new Creator<Assessment>() {
        @Override
        public Assessment createFromParcel(Parcel in) {
            return new Assessment(in);
        }

        @Override
        public Assessment[] newArray(int size) {
            return new Assessment[size];
        }
    };

    // Describe the kinds of special objects contained in this Parcelable's marshalled representation
    @Override
    public int describeContents() {
        return 0;
    }

    // Flatten this object into a Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (assID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(assID);
        }
        if (classID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(classID);
        }
        // Convert non-parcelable parameters to strings so that they can be converted
        dest.writeString(dueDate.format(formatter));
        dest.writeString(assType.name());
        dest.writeString(assTitle);
    }

    // Getter for due date
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Getter for start date
    public LocalDate getStartDate() {
        return startDate;
    }

    // Getter for assessment ID
    public Integer getAssessmentId() {
        return assID;
    }

    // Getter for assessment type
    public AssessmentType getAssessmenType() {
        return assType;
    }
    // Getter for assessment name
    public String getAssessmentName() {
        return assTitle;
    }

    // Setter for class ID
    public void setClassID(Integer classID) {
        this.classID = classID;
    }

}
