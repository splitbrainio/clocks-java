package io.splitbrain.util;

import java.util.Collection;

/**
 * Instances implementing this interface provide an idempotent, associative and commutative method for
 * combining themselves. To gain an intuition for what these properties mean in the context of a {@link #merge(T , T)}
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
 *   properties:
 *   <pre>
 *   {@code
 *   Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3));
 *   Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5));
 *   set1.addAll(set2); // Resulting set1: {1, 2, 3, 4, 5}
 *   set2.addAll(set1); // Resulting set2: {1, 2, 3, 4, 5} - same as set1, demonstrating idempotence, associativity, and commutativity.
 *   }
 * <p>
 *
 * @param <T> the type which provides a merge function

 */
public interface Mergeable<T> {

    /**
     * Merges two instances of type T together into a single instance, ensuring that
     * the merge operation is idempotent, associative, and commutative.
     * <p>
     * <em>Note:</em> implementations are recommended but not required to be immutable, so the returned T may be either
     * input with mutated contents.
     *
     * @param l An instance of T to be merged
     * @param r An instance of T to be merged
     * @return An instance of T that is the result of merging l and r
     */
    T merge(T l, T r);
}
