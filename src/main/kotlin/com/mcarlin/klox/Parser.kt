package com.mcarlin.klox

import java.lang.RuntimeException

class Parser(
    private val tokens: List<Token>,
) {
    private var current: Int = 0

    fun parse(): Expr? {
        try {
            return expression()
        } catch (e: ParseError) {
            return null
        }
    }

    private fun expression(): Expr {
        return equality()
    }

    private fun equality(): Expr {
        var expr = comparison()
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Binary(expr, operator, right)
        }
        return expr
    }

    private fun previous(): Token = tokens.getOrElse(current - 1) { tokens[0] }

    private fun match(vararg tokens: TokenType): Boolean {
        tokens.forEach {
            if (check(it)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun advance(): Token {
        if (!isAtEnd()) {
            current++
        }
        return previous()
    }

    private fun check(tokenType: TokenType): Boolean {
        if (isAtEnd()) {
            return false
        }
        return peek().type == tokenType
    }

    private fun peek(): Token = tokens[current]

    private fun isAtEnd(): Boolean = (tokens.size - 1) == current

    private fun comparison(): Expr{
        var expr = term()

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = term()
            expr = Binary(expr, operator, right)
        }

        return expr
    }

    private fun term(): Expr {
        var expr = factor()

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Binary(expr, operator, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right = unary()
            expr = Binary(expr, operator, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.BANG, TokenType.MINUS)) {

            val operator = previous()
            val right = unary()
            return Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        if (match(TokenType.FALSE)) {
            return Literal(false)
        }

        if (match(TokenType.TRUE)) {
            return Literal(true)
        }

        if (match(TokenType.NIL)) {
            return Literal(null)
        }

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return Literal(previous().literal)
        }

        if (match(TokenType.LEFT_PAREN)) {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression")
            return Grouping(expr)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun consume(tokenType: TokenType, message: String): Token {
        if (check(tokenType)) {
            return advance()
        }

        throw error(peek(), message)
    }

    private fun synchronize() {
        advance()

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) {
                return
            }

            when (peek().type) {
                TokenType.CLASS,
                TokenType.FUN,
                TokenType.FOR,
                TokenType.WHILE,
                TokenType.IF,
                TokenType.PRINT,
                TokenType.RETURN,
                TokenType.VAR -> return
                else -> advance()
            }
        }
    }

    fun error(token: Token, message: String): ParseError {
        com.mcarlin.klox.error(token, message)
        return ParseError()
    }

    class ParseError: RuntimeException()
}