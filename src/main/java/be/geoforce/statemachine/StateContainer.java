package be.geoforce.statemachine;

public interface StateContainer<S> {
    S getState();
    S setState(S state);
}
