package com.alexshabanov.springrestapi.exception;

public class AccountCodeIsLockedException extends ServiceException {
    public AccountCodeIsLockedException() {
    }

    public AccountCodeIsLockedException(String message) {
        super(message);
    }

    public AccountCodeIsLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountCodeIsLockedException(Throwable cause) {
        super(cause);
    }
}
