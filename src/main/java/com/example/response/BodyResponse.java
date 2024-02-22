package com.example.response;

import javax.annotation.Nullable;

public class BodyResponse<T> {
    @Nullable
    String message;

    @Nullable
    T data;

    public BodyResponse(@Nullable String message) {
        this.message = message;
    }

    public BodyResponse(@Nullable T data) {
        this.data = data;
    }
    public BodyResponse(@Nullable String message, @Nullable T data) {
        this.message = message;
        this.data = data;
    }
    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }
}