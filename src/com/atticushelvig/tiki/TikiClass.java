package com.atticushelvig.tiki;

import java.util.List;
import java.util.Map;

class TikiClass implements TikiCallable {
    final String name;
    private final Map<String, TikiFunction> methods;

    TikiClass(String name, Map<String, TikiFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    TikiFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        return null;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        TikiInstance instance = new TikiInstance(this);
        return instance;
    }

    @Override
    public String toString() {
        return name;
    }
}
