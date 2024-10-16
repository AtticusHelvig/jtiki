package com.atticushelvig.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
                "Assign   : Token name, Expr value",
                "Binary   : Expr left, Token operator, Expr right",
                "Call     : Expr callee, Token paren, List<Expr> arguments",
                "Get      : Expr object, Token name",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Logical  : Expr left, Token operator, Expr right",
                "Set      : Expr object, Token name, Expr value",
                "Super    : Token keyword, Token method",
                "This     : Token keyword",
                "Unary    : Token operator, Expr right",
                "Ternary  : Token operator, Expr first, Expr middle, Expr last",
                "Variable : Token name"));

        defineAst(outputDir, "Stmt", Arrays.asList(
                "Block      : List<Stmt> statements",
                "Class      : Token name, Expr.Variable superclass, List<Stmt.Function> methods",
                "Expression : Expr expression",
                "Function   : Token name, List<Token> params, List<Stmt> body",
                "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
                "Print      : Expr expression",
                "Return     : Token keyword, Expr value",
                "Let        : Token name, Expr initializer",
                "While      : Expr condition, Stmt body"));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = String.format("%s/%s.java", outputDir, baseName);
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.atticushelvig.tiki;\n");
        writer.println("import java.util.List;\n");
        writer.println("/**\n * Automatically created by com.atticushelvig.tool.GenerateAst\n */");
        writer.println(String.format("abstract class %s {\n", baseName));

        // The base accept() method
        writer.println("    abstract <R> R accept(Visitor<R> visitor);\n");

        defineVisitor(writer, baseName, types);

        // The AST classes
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");

        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println(String.format("    static class %s extends %s {", className, baseName));
        // Fields
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            writer.println(String.format("        final %s;", field));
        }

        // Constructor
        writer.println(String.format("\n        %s(%s){", className, fieldList));

        // Store parameters in fields
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(String.format("            this.%1$s = %1$s;", name));

        }
        writer.println("        }\n");

        // Visitor pattern
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println(String.format("            return visitor.visit%s%s(this);", className, baseName));
        writer.println("        }");

        writer.println("    }\n");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(
                    String.format("        R visit%1$s%2$s(%1$s %3$s);", typeName, baseName, baseName.toLowerCase()));
        }
        writer.println("    }\n");
    }
}
