package be.geoforce.statemachine.mock;

import be.geoforce.statemachine.StateMachine;
import org.springframework.context.ApplicationEventPublisher;

public class OrderStateMachine extends StateMachine<OrderState, OrderTransition> {

    public OrderStateMachine(ApplicationEventPublisher applicationEventPublisher) {
        super(OrderTransition.ALL, applicationEventPublisher);
    }
}
