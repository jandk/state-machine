package be.geoforce.statemachine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetUtils {
    public static <T> Set<T> of(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }
}
