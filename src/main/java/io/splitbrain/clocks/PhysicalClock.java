package io.splitbrain.clocks;

import java.time.Clock;
import java.time.Instant;

/**
 * Instances implementing this interface must  produce values which correspond to so-called wall-clock time.
 */
public interface PhysicalClock extends PartiallyComparable<PhysicalClock> {

    static PhysicalClock wrap(Clock clock) {
        return new WrappingPhysicalClock(clock);
    };

    Instant getInstant();
}
