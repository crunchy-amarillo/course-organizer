package com.iu.course_organizer.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "learning_unit_note", foreignKeys = @ForeignKey(entity = LearningUnit.class, parentColumns = "uid", childColumns = "learning_unit", onUpdate = CASCADE, onDelete = CASCADE))
public class LearningUnitNote {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Integer uid;

    @Nullable
    @ColumnInfo(name = "note")
    public String note;

    @Nullable
    @ColumnInfo(name="picture_path")
    public String picturePath;

    @NonNull
    @ColumnInfo(name = "learning_unit")
    public Integer learningUnit;

    public LearningUnitNote(@NonNull String note,@Nullable String picturePath, @NonNull Integer learningUnit
    ) {
        this.note = note;
        this.picturePath = picturePath;
        this.learningUnit = learningUnit;
    }
}
