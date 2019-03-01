package be.geoforce.statemachine;

import be.geoforce.statemachine.exceptions.IllegalTransitionException;
import be.geoforce.statemachine.mock.Order;
import be.geoforce.statemachine.mock.OrderState;
import be.geoforce.statemachine.mock.OrderStateMachine;
import be.geoforce.statemachine.mock.OrderTransition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StateMachineTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private OrderStateMachine orderStateMachine;

    @Before
    public void setUp() {
        orderStateMachine = new OrderStateMachine(eventPublisher);
    }

    @Test
    public void findTransitionWhenPresent() {
        OrderTransition transition = orderStateMachine.findTransition(OrderState.PLACED, OrderState.CANCELLED);

        assertThat(transition).isEqualTo(OrderTransition.CANCEL);
    }

    @Test
    public void findTransitionWhenMissing() {
        OrderTransition transition = orderStateMachine.findTransition(OrderState.CANCELLED, OrderState.PROCESSING);

        assertThat(transition).isNull();
    }

    @Test
    public void transitionToValidState() {
        Order order = new Order(OrderState.AWAITING_PAYMENT);

        Order result = orderStateMachine.transition(order, OrderState.PAID);
        assertThat(result.getState()).isEqualTo(OrderState.PAID);
    }

    @Test
    public void transitionToInValidState() {
        Order order = new Order(OrderState.CANCELLED);

        assertThatThrownBy(() -> orderStateMachine.transition(order, OrderState.PAID))
                .isInstanceOf(IllegalTransitionException.class)
                .hasMessage("Can not transition from CANCELLED to PAID");

        verify(eventPublisher, never()).publishEvent(any(TransitionEvent.class));
        assertThat(order.getState()).isEqualTo(OrderState.CANCELLED);
    }

    @Test
    public void transitionWithNullContainer() {
        Order order = new Order(OrderState.CANCELLED);

        assertThatThrownBy(() -> orderStateMachine.transition(null, OrderState.PAID))
                .isInstanceOf(IllegalArgumentException.class);

        verify(eventPublisher, never()).publishEvent(any(TransitionEvent.class));
        assertThat(order.getState()).isEqualTo(OrderState.CANCELLED);
    }

    @Test
    public void transitionWithNullToState() {
        Order order = new Order(OrderState.CANCELLED);

        assertThatThrownBy(() -> orderStateMachine.transition(order, null))
                .isInstanceOf(IllegalArgumentException.class);

        verify(eventPublisher, never()).publishEvent(any(TransitionEvent.class));
        assertThat(order.getState()).isEqualTo(OrderState.CANCELLED);
    }
}
