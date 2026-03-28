package com.abdullah.misar.exception;

public class AlreadyCheckedInException extends RuntimeException {
    public AlreadyCheckedInException() {
        super("You have already submitted a check-in for today");
    }
}
