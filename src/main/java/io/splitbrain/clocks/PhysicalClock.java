package io.splitbrain.clocks;

import io.splitbrain.util.PartialComparison;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Simple wrapper class for adapting built in java clocks to our interfaces.
 * <p>
 * Provides wall clock timestamps of millisecond precision.
 */
class PhysicalClock implements ImmutableClock<PhysicalClock, Instant> {

    private static final Clock EPOCH = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
    private final Clock wrapped;
    private final Instant timestamp;

    PhysicalClock(Clock wrapped) {
        this.wrapped = wrapped;
        this.timestamp = EPOCH.instant();
    }

    private PhysicalClock(Clock wrapped, Instant timestamp) {
        this.wrapped = wrapped;
        this.timestamp = timestamp;
    }

    @Override
    public PhysicalClock tick() {
        var newTimestamp = wrapped.instant();
        return new PhysicalClock(wrapped, newTimestamp);
    }

    @Override
    public PhysicalClock startOfTime() {
        return new PhysicalClock(wrapped);
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public PartialComparison compareTo(PhysicalClock other) {
        if (getTimestamp().equals(other)) {
            return PartialComparison.EQUAL;
        } else if (getTimestamp().isAfter(other.getTimestamp())) {
            return PartialComparison.GREATER_THAN;
        } else if (getTimestamp().isBefore(other.getTimestamp())) {
            return PartialComparison.LESS_THAN;
        } else {
            return PartialComparison.INDISTINGUISHABLE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalClock that)) return false;
        return Objects.equals(wrapped, that.wrapped) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrapped, timestamp);
    }
}
