package io.splitbrain.clocks;

import java.util.Optional;

/**
 * Instances implementing this interface can be compared with one and other, producing a {@link PartialComparison} result.
 */
interface PartiallyComparable<T> {
    PartialComparison compareTo(T other);

    static <T extends PartiallyComparable<T>> Optional<T> min(T l, T r) {
        var comparison = l.compareTo(r);
        return switch (comparison) {
            case EQUAL, LESS_THAN -> Optional.ofNullable(l);
            case GREATER_THAN -> Optional.ofNullable(r);
            case INDISTINGUISHABLE -> Optional.empty();
        };
    }

    static <A extends PartiallyComparable<A>> Optional<A> max(A l, A r) {
        var comparison = l.compareTo(r);
        return switch (comparison) {
            case EQUAL, GREATER_THAN -> Optional.ofNullable(l);
            case LESS_THAN -> Optional.ofNullable(r);
            case INDISTINGUISHABLE -> Optional.empty();
        };
    }
}
