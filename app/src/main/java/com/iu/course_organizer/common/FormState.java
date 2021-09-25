package com.iu.course_organizer.common;

public class FormState {
    protected boolean isDataValid;

    public FormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
