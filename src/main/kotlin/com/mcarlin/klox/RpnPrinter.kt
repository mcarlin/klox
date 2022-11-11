package com.mcarlin.klox

class RpnPrinter: Expr.Visitor<String>{
    fun print(expr: Expr): String {
        return expr.accept(this)
    }
    override fun visitBinaryExpr(binary: Binary): String = "${binary.left.accept(this)} ${binary.right.accept(this)} ${binary.operator.lexeme}"


    override fun visitGroupingExpr(grouping: Grouping): String = grouping.expression.accept(this)

    override fun visitLiteralExpr(literal: Literal): String {
        return if (literal.value != null) {
            literal.value.toString()
        } else {
            "nil"
        }
    }

    override fun visitUnaryExpr(unary: Unary): String = "${unary.right.accept(this)} ${unary.operator.lexeme}"
}

fun main() {
    val expression = Binary(
        Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Literal(123)
        ),
        Token(TokenType.STAR, "*", null, 1),
        Grouping(Literal(45.67))
    )
    println(RpnPrinter().print(expression))
    val expression2 = Binary(
        Grouping(
            Binary(
                Literal(1),
                Token(TokenType.PLUS, "+", null, 1),
                Literal(2)
            )
        ),
        Token(TokenType.STAR, "*", null, 1),
        Grouping(
            Binary(
                Literal(4),
                Token(TokenType.MINUS, "-", null, 1),
                Literal(3)
            )
        )
    )
    println(RpnPrinter().print(expression2))
}