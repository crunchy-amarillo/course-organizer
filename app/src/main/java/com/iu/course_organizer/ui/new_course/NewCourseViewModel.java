package com.iu.course_organizer.ui.new_course;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.data.Result;


public class NewCourseViewModel extends ViewModel {
    private MutableLiveData<NewCourseFormState> formState = new MutableLiveData<>();
    private MutableLiveData<DefaultResult<Boolean>> addResult = new MutableLiveData<>();
    private CourseRepository courseRepository;

    public NewCourseViewModel(@NonNull CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    LiveData<NewCourseFormState> getFormState() {
        return formState;
    }

    LiveData<DefaultResult<Boolean>> getResult() {
        return addResult;
    }

    public void add(String courseTitle, String courseDescription, String courseSession, Integer userId) {
        Thread thread = new Thread(() -> {
            Result<Void> result = courseRepository.add(courseTitle, courseDescription, courseSession, userId);
            if (result instanceof Result.Success) {
                addResult.postValue(new DefaultResult<>(true));
            } else {
                addResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }

    public void dataChanged(String courseTitle, String courseDescription, String courseSession) {
        if (!StringUtils.isNotEmpty(courseTitle)) {
            formState.setValue(new NewCourseFormState(R.string.invalid_empty, null, null));
        } else if (!StringUtils.isNotEmpty(courseSession)) {
            formState.setValue(new NewCourseFormState(null, null, R.string.invalid_empty));
        } else {
            formState.setValue(new NewCourseFormState(true));
        }
    }
}
