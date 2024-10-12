package com.atticushelvig.tiki;

import java.util.List;

/**
 * Automatically created by com.atticushelvig.tool.GenerateAst
 */
abstract class Expr {
    static class Binary extends Expr {
        final Expr left;
        final Token operator;
        final Expr right;

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }

    static class Grouping extends Expr {
        final Expr expression;

        Grouping(Expr expression) {
            this.expression = expression;
        }
    }

    static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }
    }

    static class Unary extends Expr {
        final Token operator;
        final Expr right;

        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
    }
}
