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

    public Result<Void> edit(String title, String description, String workingHours,
            Integer learningUnitId
    ) {
        try {
            LearningUnit learningUnit = database.learningUnitDao().findById(learningUnitId);
            learningUnit.title = title;
            learningUnit.description = description;
            learningUnit.workingHours = Integer.valueOf(workingHours);

            database.learningUnitDao().update(learningUnit);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error on updating learning unit", e));
        }
    }

    public Result<Void> increaseSpentMinutes(Integer learningUnitId, Integer minutes) {
        try {
            LearningUnit learningUnit = database.learningUnitDao().findById(learningUnitId);
            learningUnit.spentMinutes =
                    (null == learningUnit.spentMinutes ? 0 : learningUnit.spentMinutes) + minutes;

            database.learningUnitDao().update(learningUnit);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(
                    new IOException("Error on increasing spent minutes for learning unit", e));
        }
    }

    public Result<LearningUnit> findById(Integer learningUnitId) {
        try {
            LearningUnit learningUnit = database.learningUnitDao().findById(learningUnitId);
            return new Result.Success<LearningUnit>(learningUnit);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching learning unit", e));
        }

    }

    public Result<List<LearningUnit>> findByCourseId(Integer courseId) {
        try {
            List<LearningUnit> learningUnits = database.learningUnitDao().findByCourseId(courseId);
            return new Result.Success<List<LearningUnit>>(learningUnits);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching learning units", e));
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
