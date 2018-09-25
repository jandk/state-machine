package be.geoforce.statemachine;

import com.google.common.collect.ImmutableSet;

public interface Transition<S extends State> {
    ImmutableSet<S> getFrom();
    S getTo();
}
