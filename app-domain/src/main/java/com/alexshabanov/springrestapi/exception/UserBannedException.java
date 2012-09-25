package com.alexshabanov.springrestapi.exception;

public class UserBannedException extends ServiceException {
    public UserBannedException() {
    }

    public UserBannedException(String message) {
        super(message);
    }

    public UserBannedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserBannedException(Throwable cause) {
        super(cause);
    }
}
