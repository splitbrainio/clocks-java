package io.splitbrain.clocks;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Simple wrapper class for adapting built in java clocks to our interfaces
 */
class WrappingPhysicalClock implements PhysicalClock {

    private final Clock wrapped;

    WrappingPhysicalClock(Clock wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Instant getInstant() {
        return wrapped.instant();
    }

    @Override
    public PartialComparison compareTo(PhysicalClock other) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrappingPhysicalClock that)) return false;
        return Objects.equals(wrapped, that.wrapped);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrapped);
    }
}
