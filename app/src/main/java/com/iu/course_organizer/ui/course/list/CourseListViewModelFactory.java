package com.iu.course_organizer.ui.course.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;

public class CourseListViewModelFactory implements ViewModelProvider.Factory {
    private CourseOrganizerDatabase database;

    public CourseListViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CourseListViewModel.class)) {
            return (T) new CourseListViewModel(CourseRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
