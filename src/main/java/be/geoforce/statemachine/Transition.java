package be.geoforce.statemachine;

import com.google.common.collect.ImmutableSet;

public interface Transition<S> {
    ImmutableSet<S> getFrom();
    S getTo();
}
