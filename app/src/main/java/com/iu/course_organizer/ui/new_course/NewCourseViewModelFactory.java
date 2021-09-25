package com.iu.course_organizer.ui.new_course;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;

public class NewCourseViewModelFactory  implements ViewModelProvider.Factory {
    private CourseOrganizerDatabase database;

    public NewCourseViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewCourseViewModel.class)) {
            return (T) new NewCourseViewModel(CourseRepository.getInstance(database));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
