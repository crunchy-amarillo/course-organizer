package com.iu.course_organizer.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    public User(@NonNull String username, String firstname, String lastname, @NonNull String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Integer uid;

    @NonNull
    @ColumnInfo(name="username")
    public String username;

    @ColumnInfo(name="firstname")
    public String firstname;

    @ColumnInfo(name="lastname")
    public String lastname;

    // todo: encrypt password
    @NonNull
    @ColumnInfo(name="password")
    public String password;

    public static User[] populateData() {
        return new User[] {
                new User("user1", "Peter", "Silie", "Abcd1234"),
                new User("user2", "Klara", "Fall", "1234abcD")
        };
    }
}
