package com.iu.course_organizer.ui.course.add;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.FormState;

public class AddCourseFormState extends FormState {
    @Nullable
    private Integer courseTitleError;

    @Nullable
    private Integer courseDescriptionError;

    @Nullable
    private Integer courseSessionError;

    public AddCourseFormState(boolean isDataValid) {
        super(isDataValid);
    }

    public AddCourseFormState(@Nullable Integer courseTitleError, @Nullable Integer courseDescriptionError, @Nullable Integer courseSessionError) {
        super(false);
        this.courseTitleError = courseTitleError;
        this.courseDescriptionError = courseDescriptionError;
        this.courseSessionError = courseSessionError;
    }

    @Nullable
    public Integer getCourseTitleError() {
        return courseTitleError;
    }

    @Nullable
    public Integer getCourseDescriptionError() {
        return courseDescriptionError;
    }

    @Nullable
    public Integer getCourseSessionError() {
        return courseSessionError;
    }
}
