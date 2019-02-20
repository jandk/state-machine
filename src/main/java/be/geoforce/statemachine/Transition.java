package be.geoforce.statemachine;

import java.util.Set;

public interface Transition<S extends State> {
    Set<S> getFrom();
    S getTo();
}
