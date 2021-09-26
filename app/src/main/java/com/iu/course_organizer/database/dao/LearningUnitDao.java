package com.iu.course_organizer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.iu.course_organizer.database.model.Course;
import com.iu.course_organizer.database.model.LearningUnit;

import java.util.List;

@Dao
public interface LearningUnitDao {
    @Query("SELECT * FROM learning_unit WHERE course = :courseId ORDER BY title COLLATE NOCASE")
    List<LearningUnit> findByCourseId(Integer courseId);

    @Query("SELECT * FROM learning_unit WHERE uid = :learningUnitId")
    LearningUnit findById(Integer learningUnitId);

    @Insert
    void insert(LearningUnit learningUnit);

    @Delete
    void delete(LearningUnit learningUnit);
}
