package com.iu.course_organizer.ui.course.add;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;

public class AddCourseViewModelFactory implements ViewModelProvider.Factory {
    private CourseOrganizerDatabase database;

    public AddCourseViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddCourseViewModel.class)) {
            return (T) new AddCourseViewModel(CourseRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
