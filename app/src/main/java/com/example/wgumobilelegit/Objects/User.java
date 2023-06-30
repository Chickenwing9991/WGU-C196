package com.example.wgumobilelegit.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "users")
public class User implements Parcelable {
    @PrimaryKey (autoGenerate = true)
    public Integer userID;
    @ColumnInfo (name = "user")
    public String user;
    @ColumnInfo (name = "password")
    public String password;


    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }


    protected User(Parcel in) {
        if (in.readByte() == 0) {
            userID = null;
        } else {
            userID = in.readInt();
        }
        user = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserPassword() {
        return password;
    }
    public Integer getUserId() {
        return userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "Password{" +
                "User='" + user + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userID);

        }
        dest.writeString(user);
        dest.writeString(password);
    }
}
