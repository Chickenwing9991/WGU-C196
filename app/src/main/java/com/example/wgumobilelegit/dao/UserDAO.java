package com.example.wgumobilelegit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wgumobilelegit.Objects.User;

import java.util.List;


@Dao
public interface UserDAO {

    @Query("SELECT * FROM users WHERE userid IS :id")
    List<User> getAllUsers(int id);

    @Query("SELECT user FROM users WHERE userid IS :id")
    User getUserName(int id);

    @Query("SELECT password FROM users WHERE userid IS :id")
    User getUserPassword(int id);

    @Query("SELECT userid FROM users WHERE user IS :user")
    User getUserId(String user);

    @Query("DELETE FROM users")
    void deleteAllRows();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
