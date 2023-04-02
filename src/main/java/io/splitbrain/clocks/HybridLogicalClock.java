package io.splitbrain.clocks;

import io.splitbrain.util.Mergeable;
import io.splitbrain.util.PartialComparison;

import java.time.Clock;
import java.time.Duration;
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
public class HybridLogicalClock implements PhysicalClock, Mergeable<HybridLogicalClock> {

    private final PhysicalClock physicalClock;
    private final Instant timestamp;
    private final int counter;

    HybridLogicalClock() {
        this.physicalClock = new WrappingPhysicalClock(Clock.systemUTC());
        this.timestamp = physicalClock.startOfTime().getTimestamp();
        this.counter = 0;
    }

    private HybridLogicalClock(PhysicalClock physicalClock, Instant timestamp, int counter) {
        this.physicalClock = physicalClock;
        this.timestamp = timestamp;
        this.counter = counter;
    }

    @Override
    public PhysicalClock tick() {
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
    public PhysicalClock startOfTime() {
        return physicalClock.startOfTime();
    }

    @Override
    public HybridLogicalClock merge(HybridLogicalClock l, HybridLogicalClock r) {
        var previousTimestampL = l.getTimestamp();
        var previousTimestampR = r.getTimestamp();
        var nextTimestamp = laterInstant(previousTimestampL, previousTimestampR);
        var nextCounter = 0;
        if (previousTimestampL.equals(previousTimestampR)) {
            Math.max(l.counter, r.counter)
        }
        return ;
    }

    @Override
    public PartialComparison compareTo(PhysicalClock other) {
        return null;
    }

    private Instant laterInstant(Instant a, Instant b) {
        return a.isAfter(b) ? a : b;
    }

    private int counter() {
        return counter;
    }
}
