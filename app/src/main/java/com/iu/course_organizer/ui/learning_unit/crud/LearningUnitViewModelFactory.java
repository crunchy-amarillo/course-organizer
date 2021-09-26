package com.iu.course_organizer.ui.learning_unit.crud;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;

public class LearningUnitViewModelFactory implements ViewModelProvider.Factory {
    private CourseOrganizerDatabase database;

    public LearningUnitViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LearningUnitViewModel.class)) {
            return (T) new LearningUnitViewModel(LearningUnitRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
