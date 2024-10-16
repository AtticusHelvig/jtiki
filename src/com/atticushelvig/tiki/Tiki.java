package com.atticushelvig.tiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Entrypoint to jtiki Tiki interpreter
 *
 * @author Atticus Helvig
 */
public class Tiki {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jtiki [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * Reads and runs a file given its path
     *
     * @param path to the file to run
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) {
            System.exit(65);
        }
        if (hadRuntimeError) {
            System.exit(70);
        }
    }

    /**
     * Runs a read evaluate print loop
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    /**
     * Tokenizes source code
     */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        // Stop if there was a syntax error
        if (hadError) {
            return;
        }

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        // Stop if there was a resolution error
        if (hadError) {
            return;
        }

        interpreter.interpret(statements);
    }

    /**
     * Constructs an error message and reports it to the user
     *
     * @param line    that the error occured
     * @param message to report
     */
    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(String.format("%s\n[line %d]", error.getMessage(), error.token.line));
        hadRuntimeError = true;
    }

    /**
     * Report a message to the user (usually an error)
     *
     * @param line    number to reference
     * @param where   I have no idea what this is for
     * @param message to report
     */
    private static void report(int line, String where, String message) {
        System.err.printf("[line %d] Error%s: %s\n", line, where, message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, String.format(" at '%s'", token.lexeme), message);
        }
    }
}
