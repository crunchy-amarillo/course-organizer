package com.iu.course_organizer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.iu.course_organizer.database.model.Course;
import com.iu.course_organizer.database.model.LearningUnitNote;

import java.util.List;

@Dao
public interface LearningUnitNoteDao {
    @Query("SELECT * FROM learning_unit_note WHERE learning_unit = :learningUnitId")
    List<LearningUnitNote> findByLearningUnitId(Integer learningUnitId);

    @Query("SELECT * FROM learning_unit_note WHERE uid = :documentId")
    LearningUnitNote findById(Integer documentId);

    @Insert
    void insert(LearningUnitNote learningUnitNote);

    @Delete
    void delete(LearningUnitNote learningUnitNote);
}
