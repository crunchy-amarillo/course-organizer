package com.iu.course_organizer.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "uid",
        childColumns = "user",
        onUpdate = CASCADE,
        onDelete = CASCADE))
public class Course {
    public Course(@NonNull String title, @NonNull String description, @NonNull Integer session, @NonNull Integer user) {
        this.title = title;
        this.description = description;
        this.session = session;
        this.user = user;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Integer uid;

    @NonNull
    @ColumnInfo(name = "title")
    public String title;

    @NonNull
    @ColumnInfo(name = "description")
    public String description;

    @NonNull
    @ColumnInfo(name = "session")
    public Integer session;

    @NonNull
    @ColumnInfo(name = "user")
    public Integer user;
}
