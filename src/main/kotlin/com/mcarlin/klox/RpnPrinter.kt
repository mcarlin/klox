package com.mcarlin.klox

class RpnPrinter: Expr.Visitor<String>{
    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitAssignExpr(assign: Expr.Assign): String {
        return "${assign.name} = ${assign.value.accept(this)}"
    }

    override fun visitBinaryExpr(binary: Expr.Binary): String = "${binary.left.accept(this)} ${binary.right.accept(this)} ${binary.operator.lexeme}"


    override fun visitGroupingExpr(grouping: Expr.Grouping): String = grouping.expression.accept(this)

    override fun visitLiteralExpr(literal: Expr.Literal): String {
        return if (literal.value != null) {
            literal.value.toString()
        } else {
            "nil"
        }
    }

    override fun visitUnaryExpr(unary: Expr.Unary): String = "${unary.right.accept(this)} ${unary.operator.lexeme}"
    override fun visitVariableExpr(variable: Expr.Variable): String {
        return variable.name.literal.toString()
    }
}

fun main() {
    val expression = Expr.Binary(
        Expr.Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Expr.Literal(123)
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(Expr.Literal(45.67))
    )
    println(RpnPrinter().print(expression))
    val expression2 = Expr.Binary(
        Expr.Grouping(
            Expr.Binary(
                Expr.Literal(1),
                Token(TokenType.PLUS, "+", null, 1),
                Expr.Literal(2)
            )
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(
            Expr.Binary(
                Expr.Literal(4),
                Token(TokenType.MINUS, "-", null, 1),
                Expr.Literal(3)
            )
        )
    )
    println(RpnPrinter().print(expression2))
}