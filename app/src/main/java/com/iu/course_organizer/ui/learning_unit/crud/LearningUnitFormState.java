package com.iu.course_organizer.ui.learning_unit.crud;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.FormState;

public class LearningUnitFormState extends FormState {
    @Nullable
    private Integer titleError;

    @Nullable
    private Integer descriptionError;

    @Nullable
    private Integer hoursError;

    public LearningUnitFormState(boolean isDataValid) {
        super(isDataValid);
    }

    public LearningUnitFormState(@Nullable Integer titleError,
            @Nullable Integer descriptionError, @Nullable Integer hoursError
    ) {
        super(false);
        this.titleError = titleError;
        this.descriptionError = descriptionError;
        this.hoursError = hoursError;
    }

    @Nullable
    public Integer getTitleError() {
        return titleError;
    }

    @Nullable
    public Integer getDescriptionError() {
        return descriptionError;
    }

    @Nullable
    public Integer getHoursError() {
        return hoursError;
    }
}
