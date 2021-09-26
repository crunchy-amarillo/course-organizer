package com.iu.course_organizer.data;

import androidx.annotation.NonNull;

import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.LearningUnit;

import java.io.IOException;
import java.util.List;

public class LearningUnitRepository {
    private CourseOrganizerDatabase database;

    private static volatile LearningUnitRepository instance;

    public static LearningUnitRepository getInstance(@NonNull CourseOrganizerDatabase database) {
        if (instance == null) {
            instance = new LearningUnitRepository(database);
        }
        return instance;
    }

    private LearningUnitRepository(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    public Result<Void> add(String title, String description, String workingHours, Integer courseId
    ) {
        try {
            LearningUnit learningUnit =
                    new LearningUnit(title, description, Integer.valueOf(workingHours), courseId);
            database.learningUnitDao().insert(learningUnit);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error on adding learning unit", e));
        }
    }

    public Result<List<LearningUnit>> findByCourseId(Integer courseId) {
        try {
            List<LearningUnit> learningUnits = database.learningUnitDao().findByCourseId(courseId);
            return new Result.Success<List<LearningUnit>>(learningUnits);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching learing units", e));
        }
    }

    public Result<Void> delete(Integer courseId) {
        try {
            LearningUnit learningUnit = database.learningUnitDao().findById(courseId);
            database.learningUnitDao().delete(learningUnit);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while deleting learning unit", e));
        }
    }
}
