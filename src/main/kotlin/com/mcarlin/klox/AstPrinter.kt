package com.mcarlin.klox

class AstPrinter: Expr.Visitor<String> {

    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(binary: Binary): String {
        return parenthesize(binary.operator.lexeme, binary.left, binary.right);
    }

    override fun visitGroupingExpr(grouping: Grouping): String {
        return parenthesize("group", grouping.expression)
    }

    override fun visitLiteralExpr(literal: Literal): String {
        return if (literal.value != null) {
           literal.value.toString()
        } else {
            "nil"
        }
    }

    override fun visitUnaryExpr(unary: Unary): String {
        return parenthesize(unary.operator.lexeme, unary.right)
    }

    fun parenthesize(name: String, vararg exprs: Expr): String {
        val builder = StringBuilder()
        builder.append("(").append(name)
        exprs.forEach {
            builder.append(" ")
            builder.append(it.accept(this))
        }
        builder.append(")")

        return builder.toString()
    }
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
    println(AstPrinter().print(expression))
}