package com.atticushelvig.tiki;

import java.util.List;

/**
 * Automatically created by com.atticushelvig.tool.GenerateAst
 */
abstract class Stmt {

    abstract <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitExpressionStmt(Expression expression);
        R visitPrintStmt(Print print);
        R visitLetStmt(Let let);
    }

    static class Expression extends Stmt {
        final Expr expression;

        Expression(Expr expression){
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Print extends Stmt {
        final Expr expression;

        Print(Expr expression){
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static class Let extends Stmt {
        final Token name;
        final Expr initializer;

        Let(Token name, Expr initializer){
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLetStmt(this);
        }
    }

}
