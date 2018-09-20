package be.geoforce.statemachine;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class App {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private StateMachine<OrderState, OrderTransition> stateMachine = new StateMachine<>(OrderTransition.ALL, applicationEventPublisher);

    public static void main(String[] args) {

    }

    public enum OrderState {
        PLACED,
        PROCESSING,
        AWAITING_PAYMENT,
        PAID,
        CANCELLED
    }

    @Getter
    @AllArgsConstructor
    public enum OrderTransition implements Transition<OrderState> {
        PROCESS(OrderState.PLACED, OrderState.PROCESSING),
        READY(OrderState.PROCESSING, OrderState.AWAITING_PAYMENT),
        PAY(OrderState.AWAITING_PAYMENT, OrderState.PAID),
        CANCEL(ImmutableSet.of(
                OrderState.AWAITING_PAYMENT,
                OrderState.CANCELLED,
                OrderState.PLACED),
                OrderState.CANCELLED);

        private final ImmutableSet<OrderState> from;
        private final OrderState to;
        public final static ImmutableList<OrderTransition> ALL = ImmutableList.copyOf(values());

        OrderTransition(OrderState from, OrderState to) {
            this(ImmutableSet.of(from), to);
        }
    }

}


