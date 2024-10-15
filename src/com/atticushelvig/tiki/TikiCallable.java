package com.atticushelvig.tiki;

import java.util.List;

interface TikiCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}
