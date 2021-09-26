package com.iu.course_organizer.ui.learning_unit.add;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.FormState;

public class AddLearningUnitFormState extends FormState {
    @Nullable
    private Integer titleError;

    @Nullable
    private Integer descriptionError;

    @Nullable
    private Integer hoursError;

    public AddLearningUnitFormState(boolean isDataValid) {
        super(isDataValid);
    }

    public AddLearningUnitFormState(@Nullable Integer titleError,
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
