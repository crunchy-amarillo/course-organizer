package com.iu.course_organizer.ui.new_course;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.FormState;

public class NewCourseFormState extends FormState {
    @Nullable
    private Integer courseTitleError;

    @Nullable
    private Integer courseDescriptionError;

    @Nullable
    private Integer courseSessionError;

    public NewCourseFormState(boolean isDataValid) {
        super(isDataValid);
    }

    public NewCourseFormState(@Nullable Integer courseTitleError, @Nullable Integer courseDescriptionError, @Nullable Integer courseSessionError) {
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
