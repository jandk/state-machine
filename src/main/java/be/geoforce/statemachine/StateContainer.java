package be.geoforce.statemachine;

public interface StateContainer<S extends State> {
    S getState();
    void setState(S state);
}
