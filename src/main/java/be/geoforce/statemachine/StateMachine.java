package be.geoforce.statemachine;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
public class StateMachine<S, T extends Transition<S>> {
    private final ImmutableList<T> transitions;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    private void validateTransitions() {
        Set<SingleTransition<S, T>> singleTransitions = new HashSet<>();
        for (T transition : transitions) {
            for (S fromState : transition.getFrom()) {
                SingleTransition<S, T> singleTransition = new SingleTransition<>(fromState, transition.getTo(), transition);
                if (singleTransitions.contains(singleTransition)) {
                    throw new IllegalStateException(
                            String.format("Ambiguous transition from {} to {}, there is another transitions between these states",
                                    singleTransition.fromState, singleTransition.toState));
                }
                singleTransitions.add(singleTransition);
            }
        }
    }

    public <R extends StateContainer<S>> R transition(R container, S toState) {
        return transition(container, toState, null);
    }

    public <R extends StateContainer<S>> R transition(R container, S toState, Function<R, R> onTransitioned) {
        S currentState = container.getState();
        T transition = findTransition(currentState, toState);
        if (transition == null) {
            throw new IllegalStateException(String.format("Can not transition from {} to {}", currentState, toState));
        }
        TransitionEvent beforeEvent = new TransitionEvent(TransitionEvent.TransitionEventType.BEFORE, currentState, toState, transition, container);
        applicationEventPublisher.publishEvent(beforeEvent);

        container.setState(toState);
        R resultContainer = onTransitioned == null ? container: onTransitioned.apply(container);

        TransitionEvent afterEvent = new TransitionEvent(TransitionEvent.TransitionEventType.AFTER, currentState, toState, transition, container);
        applicationEventPublisher.publishEvent(afterEvent);
        return resultContainer;
    }

    public T findTransition(S fromState, S toState) {
        for (T transition : transitions) {
            if (transition.getFrom().contains(fromState) && transition.getTo().equals(toState)) {
                return transition;
            }
        }
        return null;
    }


    @Value
    private static final class SingleTransition<S, T extends Transition<S>> {
        private final S fromState;
        private final S toState;
        @Getter
        private final T transition;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SingleTransition<S, T> that = (SingleTransition<S, T>) o;
            return com.google.common.base.Objects.equal(fromState, that.fromState) &&
                    com.google.common.base.Objects.equal(toState, that.toState);
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(fromState, toState);
        }
    }
}
