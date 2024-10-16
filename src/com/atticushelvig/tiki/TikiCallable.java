package com.atticushelvig.tiki;

import java.util.List;

/**
 * Interface to know when something can be called using '()' syntax
 *
 * @author Atticus Helvig
 */
interface TikiCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}
