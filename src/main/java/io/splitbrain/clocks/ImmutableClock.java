package io.splitbrain.clocks;

/**
 * Instances implementing this interface exhibit "clock-like" behaviour.
 * They can be used for determining partial ordering between "events" such as inter-process messages, or updates to a
 * distributed data structure.
 * <p>
 * Implementations should be immutable, producing a new instance on each call to {@link #tick()} with a <strong>greater</strong> value.
 * Implementations are not <strong>required</strong> to have a physical representation of time (i.e they may be purely logical).
 *
 * @param <T> The type of the clock itself
 * @param <V> The type of timestamps which may be read from the clock
 */
public interface ImmutableClock<T, V> extends PartiallyComparable<T> {

    /**
     * @return a new {@link ImmutableClock} instance which is strictly greater than this instance, when compared
     */
    T tick();

    /**
     * @return a snapshot of the current "time" according to this clock
     */
    V getTimestamp();

    /**
     * The "start of time" corresponds to the smallest possible value encapsulated by this clock type.
     * For example, for some physical clocks that might be the unix epoch,
     * for some logical clocks that might be the long value 0.
     * <p>
     * The clock returned by this method should be less than or equal to all other possible values of this clock type.
     *
     * @return a new {@link ImmutableClock} instance which is set to the start of "time"
     */
    T startOfTime();

    /**
     * TODO: implement merge.
     */
}
