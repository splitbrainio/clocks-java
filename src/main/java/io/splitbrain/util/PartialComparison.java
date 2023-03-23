package io.splitbrain.util;

/**
 * Enum encoding the outcomes when you compare values which are not required to have strict
 * less-than, greater-than or equal relationships.
 * <p>
 * Examples of such values are integer ranges, or some kinds of timestamp.
 * <p>
 * Take the ranges {@code [1,5)} and {@code [3,5)}, which is greater than the other?
 */
public enum PartialComparison {
    LESS_THAN((byte) 0),
    GREATER_THAN((byte) 1),
    EQUAL((byte) 2),
    INDISTINGUISHABLE((byte) 3);

    private final byte code;
    private static final PartialComparison[] values = {LESS_THAN, GREATER_THAN, EQUAL, INDISTINGUISHABLE};

    PartialComparison(byte code) {
        this.code = code;
    }

    byte code() {
        return code;
    }

    public static PartialComparison fromCode(byte code) {
        if(code < 0 || code >= values.length) {
            throw new IllegalArgumentException(String.format("Could not lookup a PartialComparison value given code %d." +
                    " Expected between 0 and 3.", code));
        }

        return values[code];
    }
}
