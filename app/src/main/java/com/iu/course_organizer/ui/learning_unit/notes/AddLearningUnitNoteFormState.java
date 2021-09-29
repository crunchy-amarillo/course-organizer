package com.iu.course_organizer.ui.learning_unit.notes;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.FormState;

public class AddLearningUnitNoteFormState extends FormState {

    @Nullable
    private Integer noteError;

    public AddLearningUnitNoteFormState(boolean isDataValid) {
        super(isDataValid);
    }

    public AddLearningUnitNoteFormState(@Nullable Integer noteError) {
        super(false);
        this.noteError = noteError;
    }

    @Nullable
    public Integer getNoteError() {
        return noteError;
    }
}
