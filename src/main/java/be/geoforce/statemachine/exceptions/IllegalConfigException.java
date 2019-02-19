package be.geoforce.statemachine.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IllegalConfigException extends RuntimeException {

    public IllegalConfigException(String message) {
        super(message);
    }
}
