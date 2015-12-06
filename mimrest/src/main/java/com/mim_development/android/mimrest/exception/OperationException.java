package com.mim_development.android.mimrest.exception;

public class OperationException extends Exception {

    public OperationException() {
    }

    public OperationException(String detailMessage) {
        super(detailMessage);
    }

    public OperationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OperationException(Throwable throwable) {
        super(throwable);
    }
}
