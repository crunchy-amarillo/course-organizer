package com.iu.course_organizer.ui.login;

import androidx.annotation.Nullable;

import com.iu.course_organizer.common.DefaultResult;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult extends DefaultResult<LoggedInUserView> {
    public LoginResult(@Nullable Integer error) {
        super(error);
    }

    public LoginResult(@Nullable LoggedInUserView success) {
        super(success);
    }
}