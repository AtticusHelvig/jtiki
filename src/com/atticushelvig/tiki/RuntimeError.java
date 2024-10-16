package com.atticushelvig.tiki;

/**
 * A simple Exception to notify Tiki about RuntimeErrors
 *
 * @author Atticus Helvig
 */
class RuntimeError extends RuntimeException {
    final Token token;

    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
