package be.geoforce.statemachine;

import be.geoforce.statemachine.exceptions.IllegalTransitionException;
import be.geoforce.statemachine.mock.Order;
import be.geoforce.statemachine.mock.OrderState;
import be.geoforce.statemachine.mock.OrderStateMachine;
import be.geoforce.statemachine.mock.OrderTransition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StateMachineTest {

    private OrderStateMachine orderStateMachine;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

        expectedException.expect(IllegalTransitionException.class);
        expectedException.expectMessage("Can not transition from CANCELLED to PAID");

        orderStateMachine.transition(order, OrderState.PAID);

        verify(eventPublisher, never()).publishEvent(any(TransitionEvent.class));
        assertThat(order.getState()).isEqualTo(OrderState.CANCELLED);
    }

    @Test
    public void transitionWithNullContainer() {
        Order order = new Order(OrderState.CANCELLED);

        expectedException.expect(IllegalArgumentException.class);

        orderStateMachine.transition(null, OrderState.PAID);

        verify(eventPublisher, never()).publishEvent(any(TransitionEvent.class));
        assertThat(order.getState()).isEqualTo(OrderState.CANCELLED);
    }

    @Test
    public void transitionWithNullToState() {
        Order order = new Order(OrderState.CANCELLED);

        expectedException.expect(IllegalArgumentException.class);

        orderStateMachine.transition(order, null);

        verify(eventPublisher, never()).publishEvent(any(TransitionEvent.class));
        assertThat(order.getState()).isEqualTo(OrderState.CANCELLED);
    }
}
