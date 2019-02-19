package be.geoforce.statemachine.mock;

import be.geoforce.statemachine.StateContainer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order implements StateContainer<OrderState> {
    private OrderState state;
}
