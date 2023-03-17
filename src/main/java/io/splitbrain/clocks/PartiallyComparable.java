package io.splitbrain.clocks;

/**
 * Instances implementing this interface can be compared with one and other, producing a {@link PartialComparison} result.
 */
interface PartiallyComparable<A> {
    PartialComparison compareTo(A other);
}
