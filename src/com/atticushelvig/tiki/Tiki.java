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
    static boolean hadError = false;

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

        // FIXME: For now, just print the tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
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

    /**
     * Report a message to the user (usually an error)
     *
     * @param line    number to reference
     * @param where   I have no idea what this is for
     * @param message to report
     */
    private static void report(int line, String where, String message) {
        System.err.printf("[line %d] Error%s: %s", line, where, message);
        hadError = true;
    }
}
