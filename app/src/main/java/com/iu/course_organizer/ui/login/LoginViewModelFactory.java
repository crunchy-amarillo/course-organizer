package com.iu.course_organizer.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private CourseOrganizerDatabase database;

    public LoginViewModelFactory(@NonNull CourseOrganizerDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource(database)));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}