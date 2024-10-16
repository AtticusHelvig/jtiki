package com.atticushelvig.tiki;

/**
 * Represents a 'return' statement
 * Useful to unwind the call stack when a 'return' is found
 *
 * @author Atticus Helvig
 */
class Return extends RuntimeException {
    final Object value;

    Return(Object value) {
        super(null, null, false, false);
        this.value = value;
    }
}
