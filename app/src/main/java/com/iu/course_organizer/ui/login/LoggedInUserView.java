package com.iu.course_organizer.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private Integer userId;

    private String displayName;

    LoggedInUserView(Integer userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    public Integer getUserId() {
        return userId;
    }
}