package com.iu.course_organizer.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.iu.course_organizer.database.model.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course WHERE user = :userId ORDER BY session, title COLLATE NOCASE")
    List<Course> findByUserId(Integer userId);

    @Query("SELECT * FROM course WHERE uid = :courseId")
    Course findById(Integer courseId);

    @Insert
    void insert(Course course);

    @Delete
    void delete(Course course);
}
