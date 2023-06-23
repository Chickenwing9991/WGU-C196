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

@Entity(tableName = "assessments")
//These objects needed to be passed through multiple activities, implementing the parcelable
// interface allowed me to pass the objects successfully with some adjustments to the 'parse' methods.
public class Assessment implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Integer assID;
    @ColumnInfo
    public Integer classID;
    @ColumnInfo
    public String assTitle;
    @ColumnInfo
    public AssessmentType assType;
    @ColumnInfo
    public LocalDate dueDate;

    @Ignore
    public Assessment(Integer assID, Integer classID, String assTitle, AssessmentType assType,
                       LocalDate dueDate) {
        this.assID = assID;
        this.classID = classID;
        this.assTitle = assTitle;
        this.assType = assType;
        this.dueDate = dueDate;
    }

    public Assessment(Integer assID, String assTitle, AssessmentType assType,
                      LocalDate dueDate) {
        this.assID = assID;
        this.assTitle = assTitle;
        this.assType = assType;
        this.dueDate = dueDate;
    }

    @Ignore
    public Assessment(String assTitle, AssessmentType assType,
                      LocalDate dueDate) {
        this.assTitle = assTitle;
        this.assType = assType;
        this.dueDate = dueDate;
    }

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
        //Convert non-parcelable parameters into the object.
        dueDate = LocalDate.parse(in.readString());
        assType = fromAssessmentString(in.readString());
        assTitle = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

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
        //Convert non-parcelable parameters to strings so that they can be converted.
        dest.writeString(dueDate.format(formatter));
        dest.writeString(assType.name());
        dest.writeString(assTitle);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Integer getAssessmentId() {
        return assID;
    }

    public AssessmentType getAssessmenType() {
        return assType;
    }
    public String getAssessmentName() {
        return assTitle;
    }

    public void setClassID(Integer classID) {
        this.classID = classID;
    }

}
