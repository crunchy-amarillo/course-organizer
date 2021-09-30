package com.iu.course_organizer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iu.course_organizer.database.model.LearningUnit;

import java.util.List;

@Dao
public interface LearningUnitDao {
    @Query("SELECT * FROM learning_unit WHERE course = :courseId ORDER BY title COLLATE NOCASE")
    List<LearningUnit> findByCourseId(Integer courseId);

    @Query("SELECT * FROM learning_unit WHERE uid = :learningUnitId")
    LearningUnit findById(Integer learningUnitId);

    @Query("SELECT learning_unit.* FROM learning_unit INNER JOIN course ON course.uid = learning_unit.course WHERE course.user = :userId")
    List<LearningUnit> findByUserId(Integer userId);

    @Update
    void update(LearningUnit learningUnit);

    @Insert
    void insert(LearningUnit learningUnit);

    @Delete
    void delete(LearningUnit learningUnit);
}
