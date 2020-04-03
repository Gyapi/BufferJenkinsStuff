package com.epam.training.danielvekas.genericbuffer.buffer;

public class GenericCircularBufferException extends Exception {

    public GenericCircularBufferException(String message) {
        super(message);
    }

    public GenericCircularBufferException(String message, Throwable cause) {
        super(message, cause);
    }
}
