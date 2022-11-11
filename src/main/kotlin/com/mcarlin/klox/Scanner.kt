package com.mcarlin.klox

class Scanner(
    private val source: String,
) {
    private val tokens = mutableListOf<Token>()
    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '/' -> {
                if (match('/')) {
                   handleLineComment()
                }
                else if (match('*')) {
                    handleBlockComment()

                }
                else {
                    addToken(TokenType.SLASH)
                }
            }

            ' ', '\r', '\t' -> {
                // ignore
            }

            '\n' -> line++
            '"' -> string()
            'o' -> {
                if (match('r')) {
                    addToken(TokenType.OR)
                }
            }

            else -> {
                if (isDigit(c)) {
                    number()
                } else if (isAlpha(c)) {
                    identifier()
                } else {
                    error(line, "Unexpected character.")
                }
            }
        }
    }

    private fun handleBlockComment() {
        var count = 1
        while (count != 0) {
            val current = peek()
            val next = peekNext()
            if (current == '/' && next == '*') {
                count++
                advance() // make sure we advance twice to cover current and next
            } else if (current == '*' && next == '/') {
                count--
                advance() // make sure we advance twice to cover current and next
            } else if (current == '\n') {
                line++
            }
            advance()
        }
    }

    private fun handleLineComment() {
        while (peek() != '\n' && !isAtEnd()) {
            advance()
        }
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) {
            advance()
    }

        val text = source.substring(start until current)
        val type = Companion.keywords.getOrElse(text) { TokenType.IDENTIFIER }
        addToken(type)
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' ||
                c in 'A'..'Z' ||
                c == '_'
    }

    private fun number() {
        while (isDigit(peek())) {
            advance()
        }

        // Look for fractional part
        if (peek() == '.' && isDigit(peekNext())) {
            // consume the `.`
            advance()
            while (isDigit(peek())) {
                advance()
            }
        }

        addToken(TokenType.NUMBER, source.substring(start until current).toDouble())
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length) {
            return 0.toChar()
        }

        return source[current + 1]
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++
            }
            advance()
        }

        if (isAtEnd()) {
            error(line, "Unterminated string.")
            return
        }

        advance() // closing `"`

        val value = source.substring(start + 1 until current)
        addToken(TokenType.STRING, value)
    }

    private fun peek(): Char {
        if (isAtEnd()) return 0.toChar()
        return source[current]
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) advance()

        current++
        return true
    }

    private fun addToken(tokenType: TokenType) {
        addToken(tokenType, null)
    }

    private fun addToken(tokenType: TokenType, literal: Any?) {
        val text = source.substring(start until current)
        tokens.add(Token(tokenType, text, literal, line))
    }

    private fun advance(): Char = source[current++]

    private fun isAtEnd(): Boolean = current >= source.length

    object Companion {
        val keywords = mapOf(
            "and" to TokenType.AND,
            "class" to TokenType.CLASS,
            "else" to TokenType.ELSE,
            "false" to TokenType.FALSE,
            "for" to TokenType.FOR,
            "fun" to TokenType.FUN,
            "if" to TokenType.IF,
            "nil" to TokenType.NIL,
            "or" to TokenType.OR,
            "print" to TokenType.PRINT,
            "return" to TokenType.RETURN,
            "super" to TokenType.SUPER,
            "this" to TokenType.THIS,
            "true" to TokenType.TRUE,
            "var" to TokenType.VAR,
            "while" to TokenType.WHILE,
        )
    }

}
