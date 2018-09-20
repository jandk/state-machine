package be.geoforce.statemachine;

import lombok.Value;

@Value
public final class TransitionEvent<S, T extends Transition<S>, R extends StateContainer<S>> {
    public enum TransitionEventType {
        BEFORE,
        AFTER
    }

    private static final String TYPE_BEFORE = "transition_event.before";
    private static final String TYPE_AFTER = "transition_event.after";

    private final TransitionEventType eventType;
    private final S fromState;
    private final S toState;
    private final T transition;
    private final R container;
}
