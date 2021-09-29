package com.iu.course_organizer.ui.learning_unit.notes;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.LearningUnitNoteRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.course.add.AddCourseViewModel;

public class AddLearningUnitNoteViewModelFactory implements ViewModelProvider.Factory {
    private CourseOrganizerDatabase database;

    public AddLearningUnitNoteViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddLearningUnitNoteViewModel.class)) {
            return (T) new AddLearningUnitNoteViewModel(
                    LearningUnitNoteRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
