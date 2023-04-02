package io.splitbrain.util;

import java.util.Collection;

/**
 * Instances implementing this interface provide an idempotent, associative and commutative method for
 * combining themselves. To gain an intuition for what these properties mean in the context of a {@link #merge(T)}
 * implementation, consider two concrete types: {@link String} and {@link java.util.Set}.
 * <p>
 * - Strings seem to have a natural merge function in the concatenation operator, however
 *   concatenation is not idempotent (applying it multiple times has a different result) and
 *   not commutative (order matters), thus is not suitable for an implementation of this interface.
 *   <pre>
 *   {@code
 *   var a = "hello" + "world"; // Resulting a: "helloworld"
 *   var b = "world" + "hello"; // Resulting b: "worldhello", not equal to "hello" + "world"
 *   var multi = "hello" + "hello"; // Resulting multi: "hellohello", not equal to "hello"
 *   }
 *   </pre>
 * <p>
 * - Sets can be merged using the union operation ({@link java.util.Set#addAll(Collection)}) which satisfies all three
 *   properties: idempotence, associativity and commutativity.
 *   <pre>
 *   {@code
 *   var a = Set.of(1, 2);
 *   var b = Set.of(3);
 *   var c = Set.of(4, 5, 6);
 *   var aa = new HashSet<>(a).addAll(a); // Resulting aa: {1, 2}, equal to a
 *   var ab = new HashSet<>(a).addAll(b); // Resulting ab: {1, 2, 3}
 *   var bc = new HashSet<>(b).addAll(c); // Resulting bc: {3, 4, 5, 6}
 *   var abThenC = ab.addAll(c); // Resulting abThenC: {1, 2, 3, 4, 5, 6}, equal to aThenBc and bca
 *   var aThenBC = a.addAll(bc); // Resulting aThenBc: {1, 2, 3, 4, 5, 6}, equal to abThenC and bca
 *   var bca = bc.addAll(a); // Resulting bca: {1, 2, 3, 4, 5, 6}, equal to abThenC and aThenBc
 *   }
 *   </pre>
 * <p>
 * <em>Note:</em> types with merge operations like these are interesting in a distributed systems context due to how
 * they are guaranteed to eventually converge when replicated, even in the presence of concurrent updates, temporary
 * network failures and duplicate message delivery.
 * <p>
 * In case you wish to read further, in academic literate implementations of this interface are sometimes referred to
 * as CRDTs or (more esoterically) join semi-lattices.
 *
 * @param <T> the type which provides a merge function
 * @see Mergeable
 */
public interface IdempotentMergeable<T> extends Mergeable<T> {

    /**
     * Merges two instances of type T together into a single instance, ensuring that
     * the merge operation is idempotent, associative, and commutative.
     * <p>
     * <em>Note:</em> implementations are recommended but not required to be immutable, so the returned T may be
     * {@code this} with mutated contents.
     *
     * @param other Another instance of T to be merged with {@code this}
     * @return An instance of T that is the result of merging r and {@code this}
     */
    T merge(T other);
}
