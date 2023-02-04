package com.mcarlin.klox

class AstPrinter: Expr.Visitor<String> {

    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitAssignExpr(assign: Expr.Assign): String {
        return "${assign.name} = ${assign.value.accept(this)}"
    }

    override fun visitBinaryExpr(binary: Expr.Binary): String {
        return parenthesize(binary.operator.lexeme, binary.left, binary.right)
    }

    override fun visitGroupingExpr(grouping: Expr.Grouping): String {
        return parenthesize("group", grouping.expression)
    }

    override fun visitLiteralExpr(literal: Expr.Literal): String {
        return if (literal.value != null) {
           literal.value.toString()
        } else {
            "nil"
        }
    }

    override fun visitUnaryExpr(unary: Expr.Unary): String {
        return parenthesize(unary.operator.lexeme, unary.right)
    }

    override fun visitVariableExpr(variable: Expr.Variable): String {
        return variable.name.literal.toString()
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String {
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
    val expression = Expr.Binary(
        Expr.Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Expr.Literal(123)
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(Expr.Literal(45.67))
    )
    println(AstPrinter().print(expression))
}