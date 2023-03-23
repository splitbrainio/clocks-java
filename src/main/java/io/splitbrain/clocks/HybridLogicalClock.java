package io.splitbrain.clocks;

import io.splitbrain.util.PartialComparison;

import java.time.Clock;
import java.time.Instant;

/**
 * Implementation of a Hybrid Logical Clock (HLC) described by Kulkarni, Demirbas et al in
 * "Logical Physical Clocks and Consistent Snapshots in Globally Distributed Databases".
 * <p>
 * Hybrid logical clocks are based on standard wall clock time (e.g. that provided by
 * `System.currentTimeMillis`), but use an additional counter in order to ensure monotonicity.
 * <p>
 * They improve upon other logical clocks (such as Lamport clocks) because they offer minimal
 * drift from their underlying wall clock over time. I.e. a single timestamp value may be used
 * both to report on real time events and infer causality.
 * <p>
 * The timestamps stored by HybridLogicalClock objects are of millisecond precision, though the
 * counter may be used to derive ordering between several objects with the same timestamp.
 */
public class HybridLogicalClock implements PhysicalClock {

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
        return null;
    }

    @Override
    public Instant getTimestamp() {
        return null;
    }

    @Override
    public PhysicalClock startOfTime() {
        physicalClock.startOfTime();
        return null;
    }

    @Override
    public PhysicalClock merge(PhysicalClock l, PhysicalClock r) {
        return null;
    }

    @Override
    public PartialComparison compareTo(PhysicalClock other) {
        return null;
    }
}
