package com.iu.course_organizer.ui.course.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseListViewModel extends ViewModel {
    private final MutableLiveData<DefaultResult<List<Course>>> courses = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> deleteResult = new MutableLiveData<>();
    private final CourseRepository courseRepository;

    public CourseListViewModel(@NonNull CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public LiveData<DefaultResult<List<Course>>> getCourses() {
        return courses;
    }

    public MutableLiveData<DefaultResult<Boolean>> getDeleteResult() {
        return deleteResult;
    }

    public void findByUserId(Integer userId) {
        Thread thread = new Thread(() -> {
            Result<List<Course>> result = courseRepository.findByUserId(userId);
            if (result instanceof Result.Success) {
                List<Course> data = (List<Course>) ((Result.Success<?>) result).getData();
                courses.postValue(new DefaultResult<>(data));
            } else {
                courses.postValue(new DefaultResult<>(new ArrayList<>()));
            }
        });
        thread.start();
    }

    public void delete(Integer courseId) {
        Thread thread = new Thread(() -> {
            Result<Void> result = courseRepository.delete(courseId);
            if (result instanceof Result.Success) {
                deleteResult.postValue(new DefaultResult<>(true));
            } else {
                deleteResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }
}
