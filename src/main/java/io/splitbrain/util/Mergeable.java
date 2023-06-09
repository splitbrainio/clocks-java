package io.splitbrain.util;

import java.util.Collection;

/**
 * Instances implementing this interface provide an associative and commutative method for
 * combining themselves. To gain an intuition for what these properties mean in the context of a {@link #merge(T)}
 * implementation, consider two concrete types: {@link String} and {@link java.util.Set}.
 * <p>
 * - Strings seem to have a natural merge function in the concatenation operator, however
 *   concatenation is not commutative (order matters), thus is not suitable for an implementation of this interface.
 *   <pre>
 *   {@code
 *   var a = "hello" + "world"; // Resulting a: "helloworld"
 *   var b = "world" + "hello"; // Resulting b: "worldhello", not equal to "hello" + "world"
 *   }
 *   </pre>
 * <p>
 * - Sets can be merged using the union operation ({@link java.util.Set#addAll(Collection)}) which satisfies both
 *   properties: associativity and commutativity.
 *   <pre>
 *   {@code
 *   var a = Set.of(1, 2);
 *   var b = Set.of(3);
 *   var c = Set.of(4, 5, 6);
 *   var ab = new HashSet<>(a).addAll(b); // Resulting ab: {1, 2, 3}
 *   var bc = new HashSet<>(b).addAll(c); // Resulting bc: {3, 4, 5, 6}
 *   var abThenC = ab.addAll(c); // Resulting abThenC: {1, 2, 3, 4, 5, 6}, equal to aThenBc and bca
 *   var aThenBC = a.addAll(bc); // Resulting aThenBc: {1, 2, 3, 4, 5, 6}, equal to abThenC and bca
 *   var bca = bc.addAll(a); // Resulting bca: {1, 2, 3, 4, 5, 6}, equal to abThenC and aThenBc
 *   }
 *   </pre>
 * <p>
 * <em>Note:</em> this interface exists separately from its more commonly used subtype {@link IdempotentMergeable}
 * because several types have a merge function which actually mutates the instance's contents, even if the merge is
 * logically a no-op. In distributed systems, such mutating merges are often used for causality tracking, relying on the
 * insight that the merge itself is an event in time, and therefore a causal boundary.
 *
 * @param <T> the type which provides a merge function
 * @see IdempotentMergeable
 */
public interface Mergeable<T> {


    /**
     * Merges two instances of type T together into a single instance, ensuring that
     * the merge operation is associative, and commutative.
     * <p>
     * <em>Note:</em> implementations are recommended but not required to be immutable, so the returned T may be
     * {@code this} with mutated contents.
     *
     * @param other Another instance of T to be merged with {@code this}
     * @return An instance of T that is the result of merging r and {@code this}
     */
    T merge(T other);
}
