package com.iu.course_organizer.ui.learning_unit.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;

public class LearningUnitListViewModelFactory implements ViewModelProvider.Factory {
    private CourseOrganizerDatabase database;

    public LearningUnitListViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LearningUnitListViewModel.class)) {
            return (T) new LearningUnitListViewModel(LearningUnitRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
