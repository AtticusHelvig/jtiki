package com.atticushelvig.tiki;

import com.atticushelvig.tiki.Expr.Binary;
import com.atticushelvig.tiki.Expr.Grouping;
import com.atticushelvig.tiki.Expr.Literal;
import com.atticushelvig.tiki.Expr.Unary;

/**
 * Visitor to print our abstract syntax trees
 *
 * @author Atticus Helvig
 * @see Visitor
 */
class AstPrinter implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) {
            return "nil";
        }
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    /**
     * Helper function to format expressions
     */
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
