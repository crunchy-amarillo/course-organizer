package com.iu.course_organizer.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.iu.course_organizer.database.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    User findByUsernameAndPassword(String username, String password);

    @Insert
    void insertAll(User... users);
}
