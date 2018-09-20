package be.geoforce.statemachine.mock;

import be.geoforce.statemachine.Transition;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
