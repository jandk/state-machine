package be.geoforce.statemachine.exceptions;

public class IllegalConfigException extends RuntimeException {
    public IllegalConfigException() {
    }

    public IllegalConfigException(String message) {
        super(message);
    }
}
