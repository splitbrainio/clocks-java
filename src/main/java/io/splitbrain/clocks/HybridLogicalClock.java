package io.splitbrain.clocks;

import io.splitbrain.util.Mergeable;
import io.splitbrain.util.PartialComparison;

import java.time.Clock;
import java.time.Instant;

/**
 * Implementation of a Hybrid Logical Clock (HLC) described by Kulkarni, Demirbas et al in
 * "Logical Physical Clocks and Consistent Snapshots in Globally Distributed Databases".
 * <p>
 * Hybrid logical clocks are based on standard wall clock time (e.g. that provided by
 * {@link System#currentTimeMillis()}), but use an additional counter in order to ensure monotonicity.
 * <p>
 * They improve upon other logical clocks (such as Lamport clocks) because they offer minimal
 * drift from their underlying wall clock over time. I.e. a single timestamp value may be used
 * both to report on real time events and infer causality.
 * <p>
 * The timestamps stored by HybridLogicalClock objects are of millisecond precision, though the
 * counter may be used to derive ordering between several objects with the same timestamp.
 */
public class HybridLogicalClock implements ImmutableClock<HybridLogicalClock, Instant>, Mergeable<HybridLogicalClock> {

    private final PhysicalClock physicalClock;
    private final Instant timestamp;
    private final int counter;

    public HybridLogicalClock() {
        this(new PhysicalClock(Clock.systemUTC()));
    }

    HybridLogicalClock(PhysicalClock physicalClock) {
        this.physicalClock = physicalClock;
        this.timestamp = physicalClock.startOfTime().getTimestamp();
        this.counter = 0;
    }

    private HybridLogicalClock(PhysicalClock physicalClock, Instant timestamp, int counter) {
        this.physicalClock = physicalClock;
        this.timestamp = timestamp;
        this.counter = counter;
    }

    @Override
    public HybridLogicalClock tick() {
        var tickedClock = physicalClock.tick();
        var physicalTimeNow = tickedClock.getTimestamp();
        var previousTimestamp = timestamp;
        var nextTimestamp = laterInstant(physicalTimeNow, previousTimestamp);
        var nextCounter = nextTimestamp.equals(previousTimestamp) ?  counter + 1 : 0;

        return new HybridLogicalClock(tickedClock, nextTimestamp, nextCounter);
    }

    @Override
    public Instant getTimestamp() {
        return physicalClock.getTimestamp();
    }

    @Override
    public HybridLogicalClock startOfTime() {
        return new HybridLogicalClock(physicalClock.startOfTime(), timestamp, counter);
    }

    @Override
    public HybridLogicalClock merge(HybridLogicalClock other) {
        var thisPreviousTimestamp = getTimestamp();
        var otherPreviousTimestamp = other.getTimestamp();
        var nextTimestamp = laterInstant(thisPreviousTimestamp, otherPreviousTimestamp);
        var nextCounter = 0;
        if (thisPreviousTimestamp.equals(otherPreviousTimestamp)) {
            nextCounter = Math.max(counter, other.counter) + 1;
        } else if (nextTimestamp.equals(otherPreviousTimestamp)) {
            nextCounter = counter + 1;
        } else {
            nextCounter = other.counter + 1;
        }

        return new HybridLogicalClock(physicalClock, nextTimestamp, nextCounter);
    }

    @Override
    public PartialComparison compareTo(HybridLogicalClock other) {
        var thisPreviousTimestamp = getTimestamp();
        var otherPreviousTimestamp = other.getTimestamp();

        var cmp = comparableToPartial(thisPreviousTimestamp, otherPreviousTimestamp);
        return cmp == PartialComparison.EQUAL ? cmp : comparableToPartial(counter, other.counter);
    }

    private <T extends Comparable<T>> PartialComparison comparableToPartial(T a, T b) {
        var cmp = a.compareTo(b);
        if (cmp > 0) {
            return PartialComparison.GREATER_THAN;
        } else if (cmp < 0) {
            return PartialComparison.LESS_THAN;
        } else {
            return PartialComparison.EQUAL;
        }
    }

    private Instant laterInstant(Instant a, Instant b) {
        return a.isAfter(b) ? a : b;
    }
}
