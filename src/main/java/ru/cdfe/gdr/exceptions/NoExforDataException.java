package ru.cdfe.gdr.exceptions;

public class NoExforDataException extends BadRequestException {
    public NoExforDataException(String subEntNumber) {
        super("Subent: " + subEntNumber);
    }
}
