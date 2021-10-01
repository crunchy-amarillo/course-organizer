package com.iu.course_organizer.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.LearningUnitNote;

import java.io.IOException;
import java.util.List;

public class LearningUnitNoteRepository {
    private CourseOrganizerDatabase database;

    private static volatile LearningUnitNoteRepository instance;

    public static LearningUnitNoteRepository getInstance(@NonNull CourseOrganizerDatabase database
    ) {
        if (instance == null) {
            instance = new LearningUnitNoteRepository(database);
        }
        return instance;
    }

    private LearningUnitNoteRepository(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    public Result<Void> add(@NonNull String note, @Nullable String picturePath, Integer learningUnitId
    ) {
        try {
            LearningUnitNote learningUnitNote = new LearningUnitNote(note, picturePath, learningUnitId);
            database.learningUnitNoteDao().insert(learningUnitNote);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error on adding learning unit note", e));
        }
    }

    public Result<List<LearningUnitNote>> findByLearningUnitId(Integer learningUnitId) {
        try {
            List<LearningUnitNote> learningUnitNotes =
                    database.learningUnitNoteDao().findByLearningUnitId(learningUnitId);
            return new Result.Success<List<LearningUnitNote>>(learningUnitNotes);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while fetching learning unit notes", e));
        }
    }

    public Result<Void> delete(Integer learningUnitId) {
        try {
            LearningUnitNote learningUnitNote =
                    database.learningUnitNoteDao().findById(learningUnitId);
            database.learningUnitNoteDao().delete(learningUnitNote);
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error while deleting learning unit note", e));
        }
    }
}
