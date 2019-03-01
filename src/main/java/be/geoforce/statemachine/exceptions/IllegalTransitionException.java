package be.geoforce.statemachine.exceptions;

public class IllegalTransitionException extends RuntimeException {
    public IllegalTransitionException() {
    }

    public IllegalTransitionException(String message) {
        super(message);
    }
}
