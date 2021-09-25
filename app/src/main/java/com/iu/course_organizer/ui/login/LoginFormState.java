package com.iu.course_organizer.ui.login;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.FormState;

/**
 * Data validation state of the login form.
 */
class LoginFormState extends FormState {
    @Nullable
    private Integer usernameError = null;
    @Nullable
    private Integer passwordError = null;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        super(false);
        this.usernameError = usernameError;
        this.passwordError = passwordError;
    }

    LoginFormState(boolean isDataValid) {
        super(isDataValid);
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }
}