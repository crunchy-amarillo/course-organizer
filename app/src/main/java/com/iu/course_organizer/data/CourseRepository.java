package com.iu.course_organizer.data;

import androidx.annotation.NonNull;

import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.Course;

import java.io.IOException;
import java.util.List;

public class CourseRepository {
    private CourseOrganizerDatabase database;

    private static volatile CourseRepository instance;

    public static CourseRepository getInstance(@NonNull CourseOrganizerDatabase database) {
        if (instance == null) {
            instance = new CourseRepository(database);
        }
        return instance;
    }

    private CourseRepository(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    public Result<Void> add(String courseTitle, String courseDescription, String courseSession,
            Integer userId
    ) {
        try {
            Course course =
                    new Course(courseTitle, courseDescription, Integer.valueOf(courseSession),
                            userId
                    );
            database.courseDao().insert(course);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error on adding course", e));
        }
    }

    public Result<Course> findById(Integer courseId) {
        try {
            Course course = database.courseDao().findById(courseId);
            return new Result.Success<Course>(course);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching courses", e));
        }
    }

    public Result<List<Course>> findByUserId(Integer userId) {
        try {
            List<Course> courses = database.courseDao().findByUserId(userId);
            return new Result.Success<List<Course>>(courses);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching courses", e));
        }
    }

    public Result<Void> delete(Integer courseId) {
        try {
            Course course = database.courseDao().findById(courseId);
            database.courseDao().delete(course);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while deleting course", e));
        }
    }
}
