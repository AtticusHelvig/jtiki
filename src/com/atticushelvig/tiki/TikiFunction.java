package com.atticushelvig.tiki;

import java.util.List;

class TikiFunction implements TikiCallable {
    private final Stmt.Function declaration;
    private final Environment closure;

    TikiFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    TikiFunction bind(TikiInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new TikiFunction(declaration, environment);
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);

        for (int i = 0; i < arity(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }
        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("<fn %s>", declaration.name.lexeme);
    }
}
