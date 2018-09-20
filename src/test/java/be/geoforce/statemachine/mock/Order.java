package be.geoforce.statemachine.mock;

import be.geoforce.statemachine.StateContainer;
import lombok.Data;

@Data
public class Order implements StateContainer<OrderState> {
    private OrderState state;
}
