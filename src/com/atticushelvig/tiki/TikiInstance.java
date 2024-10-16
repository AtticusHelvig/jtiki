package com.atticushelvig.tiki;

import java.util.HashMap;
import java.util.Map;

class TikiInstance {
    private TikiClass tikiClass;
    private Map<String, Object> fields = new HashMap<>();

    TikiInstance(TikiClass tikiClass) {
        this.tikiClass = tikiClass;
    }

    @Override
    public String toString() {
        return String.format("%s instance", tikiClass.name);
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        TikiFunction method = tikiClass.findMethod(name.lexeme);
        if (method != null) {
            return method.bind(this);
        }

        throw new RuntimeError(name, String.format("Undefined property '%s'.", name.lexeme));
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
}
