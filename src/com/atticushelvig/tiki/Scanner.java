package com.atticushelvig.tiki;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Scans Strings of source code and tokenizes them
 *
 * @author Atticus Helvig
 */
public class Scanner {
    private static final Map<String, TokenType> keywords;
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fn", TokenType.FN);
        keywords.put("if", TokenType.IF);
        keywords.put("let", TokenType.LET);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("while", TokenType.WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme (apparently)
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Scans a token and adds it to the token list
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '?':
                addToken(TokenType.CONDITONAL);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (Character.isDigit(c)) {
                    number();
                } else if (isIdentifierStart(c)) {
                    identifier();
                } else {
                    Tiki.error(line, "Unexpected character.");
                }
                break;
        }
    }

    /**
     * Consumes the next character
     *
     * @return the character consumed
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Adds a token with a given type and no value
     *
     * @param type of the token to add
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Adds a token with the given type and value
     *
     * @param type    of the token to add
     * @param literal value to assign the token
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * Consumes the character in the source file only if it matches expected
     * 
     * @param expected character to match
     * @return true if the current character matches expected, false otherwise
     */
    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    /**
     * @return the current character in the source file
     */
    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    /**
     * @return the next character in the source file
     */
    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    /**
     * Tokenizes a String
     */
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\\') {
                // NOTE: Skips characters after a '\' for escape sequences
                advance();
                if (isAtEnd()) {
                    System.err.println("SHOUT");
                    break;
                }
            }
            if (peek() == '\n') {
                line++;
            }
            advance();
        }
        if (isAtEnd()) {
            Tiki.error(line, "Unterminated string.");
            return;
        } else {
            // The closing "
            advance();
        }

        // Trim surrounding quotes and escape characters
        String value = unescape(source.substring(start + 1, current - 1));
        addToken(TokenType.STRING, value);
    }

    /**
     * Substitutes escape sequences for their escaped characters
     *
     * @param target String to modify
     * @return modified String
     */
    private String unescape(String target) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == '\\') {
                // Handles the '\' at the end of the string case
                if (i + 1 >= target.length()) {
                    throw new IllegalArgumentException("Invalid escape string.");
                }

                char c = target.charAt(++i);
                switch (c) {
                    case 't':
                        sb.append('\t');
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case '\'':
                        sb.append('\'');
                        break;
                    case '"':
                        sb.append('\"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unsupported escape sequence: '\\%c'", c));
                }
            } else {
                sb.append(target.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * Tokenizes a number
     */
    private void number() {
        while (Character.isDigit(peek())) {
            advance();
        }
        // Search for a fractional part
        if (peek() == '.' && Character.isDigit(peekNext())) {
            // Consume the '.'
            advance();
            while (Character.isDigit(peek())) {
                advance();
            }
        }
        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * Tokenizes an identifier
     */
    private void identifier() {
        while (isIdentifierPart(peek())) {
            if (peek() == '\0') {
                System.err.println("WTF?!?!");
            }
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }
        addToken(type);
    }

    private boolean isIdentifierStart(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isIdentifierPart(char c) {
        return isIdentifierStart(c) || Character.isDigit(c);
    }
}
