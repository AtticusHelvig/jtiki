package com.atticushelvig.tiki;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an environment (or scope) where variables and functions live
 *
 * @author Atticus Helvig
 */
class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    /**
     * @param distance number of Enviroments upwards in scope to get from
     * @param name     to find value of
     *
     * @return the value associated with 'name', from the Enviroment
     *         'distance' above the current
     */
    Object getAt(int distance, String name) {
        return ancestor(distance).values.get(name);
    }

    /**
     * @return the Environment 'distance' levels above the current
     */
    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }
        return environment;
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    /**
     * @param distance number of Enviroments upwards in scope to assign to
     * @param name     to assign 'value' to
     * @param value    to be assigned
     */
    void assignAt(int distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme, value);
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, String.format("Undefined variable '%s'.", name.lexeme));
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }

        throw new RuntimeError(name, String.format("Undefined variable '%s'.", name.lexeme));
    }
}
