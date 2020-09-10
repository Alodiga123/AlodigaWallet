package com.alodiga.wallet.utils;



public class KeyLongException extends Exception {

    private static final long serialVersionUID = 1L;

    public KeyLongException(String message) {
        super(message);
    }

    public KeyLongException(String message, Exception e) {
        super(message, e);
    }

    public KeyLongException(String message, StackTraceElement[] stackTrace) {
        super(message);
        setStackTrace(stackTrace);
    }
}
