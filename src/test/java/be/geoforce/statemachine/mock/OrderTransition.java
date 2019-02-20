package be.geoforce.statemachine.mock;

import be.geoforce.statemachine.SetUtils;
import be.geoforce.statemachine.Transition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum OrderTransition implements Transition<OrderState> {
    PROCESS(OrderState.PLACED, OrderState.PROCESSING),
    READY(OrderState.PROCESSING, OrderState.AWAITING_PAYMENT),
    PAY(OrderState.AWAITING_PAYMENT, OrderState.PAID),
    CANCEL(SetUtils.of(
            OrderState.AWAITING_PAYMENT,
            OrderState.CANCELLED,
            OrderState.PLACED),
            OrderState.CANCELLED);

    private final Set<OrderState> from;
    private final OrderState to;
    public final static Set<OrderTransition> ALL = SetUtils.of(values());

    OrderTransition(OrderState from, OrderState to) {
        this(SetUtils.of(from), to);
    }
}
