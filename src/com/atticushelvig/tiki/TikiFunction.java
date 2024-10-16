package com.atticushelvig.tiki;

import java.util.List;

/**
 * Represents a function in the tiki language, includes methods
 *
 * @author Atticus Helvig
 * @see TikiCallable
 */
class TikiFunction implements TikiCallable {
    private final Stmt.Function declaration;
    private final Environment closure;
    private final boolean isInitializer;

    TikiFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    /**
     * Binds a method to its class instance through the 'this' keyword
     *
     * @param instance of the class to bind to
     */
    TikiFunction bind(TikiInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new TikiFunction(declaration, environment, isInitializer);
    }

    /**
     * @return the number of arguments a function takes
     */
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
            if (isInitializer) {
                return closure.getAt(0, "this");
            }
            return returnValue.value;
        }

        if (isInitializer) {
            return closure.getAt(0, "this");
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("<fn %s>", declaration.name.lexeme);
    }
}
