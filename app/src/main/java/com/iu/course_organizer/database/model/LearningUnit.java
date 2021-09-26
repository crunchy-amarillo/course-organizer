package com.iu.course_organizer.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "learning_unit", foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "uid", childColumns = "course", onUpdate = CASCADE, onDelete = CASCADE))
public class LearningUnit {
    public LearningUnit(@NonNull String title, @NonNull String description,
            @NonNull Integer workingHours, @NonNull Integer course
    ) {
        this.title = title;
        this.description = description;
        this.workingHours = workingHours;
        this.course = course;
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
    @ColumnInfo(name = "working_hours")
    public Integer workingHours;

    @NonNull
    @ColumnInfo(name = "course")
    public Integer course;
}
