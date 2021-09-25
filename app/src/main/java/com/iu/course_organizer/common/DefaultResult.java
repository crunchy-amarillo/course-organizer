package com.iu.course_organizer.common;

import androidx.annotation.Nullable;

public class DefaultResult<T> {
    @Nullable
    private T success;

    @Nullable
    private Integer error;

    public DefaultResult(@Nullable Integer error) {
        this.error = error;
    }

    public DefaultResult(@Nullable T success) {
        this.success = success;
    }

    @Nullable
    public T getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
