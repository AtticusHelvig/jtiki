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
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = String.format("%s/%s.java", outputDir, baseName);
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.atticushelvig.tiki;\n");
        writer.println("import java.util.List;\n");
        writer.println("/**\n * Automatically created by com.atticushelvig.tool.GenerateAst\n */");
        writer.println(String.format("abstract class %s {", baseName));

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
        writer.println(String.format("        %s(%s){", className, fieldList));

        // Store parameters in fields
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(String.format("            this.%1$s = %1$s;", name));

        }
        writer.println("        }");

        writer.println("    }");
    }
}
