package com.iu.course_organizer.data;

import androidx.annotation.NonNull;

import com.iu.course_organizer.data.model.LoggedInUser;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.User;

import java.io.IOException;

public class LoginDataSource {
    private CourseOrganizerDatabase database;

    public LoginDataSource(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    public Result<LoggedInUser> login(String username, String password) {

        try {
            User user = database.userDao().findByUsernameAndPassword(username, password);
            if (null == user) {
                throw new Exception("invalid credentials");
            }

            LoggedInUser loggedInUser =
                    new LoggedInUser(
                            user.uid,
                            user.firstname + " " + user.lastname);

            return new Result.Success<>(loggedInUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}