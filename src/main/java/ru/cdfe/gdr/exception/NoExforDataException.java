package ru.cdfe.gdr.exception;

public class NoExforDataException extends RuntimeException {
    public NoExforDataException(String subEntNumber) {
        super("Subent: " + subEntNumber);
    }
}
