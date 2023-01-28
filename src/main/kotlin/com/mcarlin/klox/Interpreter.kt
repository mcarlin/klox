package com.mcarlin.klox

import kotlin.math.exp

class Interpreter: Expr.Visitor<Any?>, Stmt.Visitor<Unit> {

    fun interpret(statements: List<Stmt>) {
        try {
            statements.forEach {
                execute(it)
            }

        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }

    private fun execute(stmt: Stmt) {
        stmt.accept(this)
    }

    private fun stringify(value: Any?): String {
        if (value == null) {
           return "nil"
        }

        if (value is Double) {
            var text = value.toString()
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            return text
        }

        return value.toString()
    }

    override fun visitBinaryExpr(binary: Binary): Any? {
        val left = evaluate(binary.left)
        val right = evaluate(binary.right)

        return when (binary.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperand(binary.operator, left, right)
                (left as Double) - (right  as Double)
            }
            TokenType.SLASH -> {
                checkNumberOperand(binary.operator, left, right)
                if (right == 0) {
                    throw RuntimeError(binary.operator, "Cannot divide by 0")
                }
                (left as Double) / (right  as Double)
            }
            TokenType.STAR -> {
                checkNumberOperand(binary.operator, left, right)
                (left as Double) * (right  as Double)
            }
            TokenType.PLUS ->  {
                return if (left is Double && right is Double) {
                    left + right
                } else if (left is String && right is String) {
                    left + right
                } else if (left is String && right is Double) {
                    left + right.toString()
                }
                else if (left is Double && right is String) {
                    left.toString() + right.toString()
                }
                else {
                    throw RuntimeError(binary.operator, "Operands must be two numbers or two strings")
                }
            }
            TokenType.GREATER -> {
                checkNumberOperand(binary.operator, left, right)
                (left as Double) > (right as Double)
            }
            TokenType.GREATER_EQUAL-> {
                checkNumberOperand(binary.operator, left, right)
                (left as Double) >= (right as Double)
            }
            TokenType.LESS -> {
                checkNumberOperand(binary.operator, left, right)
                (left as Double) < (right as Double)
            }
            TokenType.LESS_EQUAL -> {
                checkNumberOperand(binary.operator, left, right)
                (left as Double) <= (right as Double)
            }
            TokenType.BANG_EQUAL -> !isEqual(left, right)
            else -> null
        }
    }

    private fun checkNumberOperand(operator: Token, operand: Any?) {
        if (operand is Double) return
        throw RuntimeError(operator, "Operand must be number")
    }
    private fun checkNumberOperand(operator: Token, left: Any?, right: Any?) {
        if (left is Double && right is Double) return
        throw RuntimeError(operator, "Operands must be numbers")
    }

    private fun isEqual(left: Any?, right: Any?): Boolean {
        return if (left == null && right == null) {
           true
        } else if (left == null) {
            false
        } else {
            left == right
        }
    }

    override fun visitGroupingExpr(grouping: Grouping): Any? {
        return evaluate(grouping.expression)
    }

    override fun visitLiteralExpr(literal: Literal): Any? {
        return literal.value
    }

    override fun visitUnaryExpr(unary: Unary): Any? {
        val right = evaluate(unary.right)

        return when (unary.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperand(unary.operator, right)
                -(right as Double)
            }
            TokenType.BANG -> !isTruthy(right)
            else -> null
        }
    }

    private fun isTruthy(any: Any?): Boolean {
        return when (any) {
            null -> false
            is Boolean -> any
            else -> true
        }
    }

    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }

    override fun visitExpressionStmt(expression: Expression) {
        evaluate(expression.expression)
    }

    override fun visitPrintStmt(print: Print) {
        val value = evaluate(print.expression)
        println(stringify(value))
    }
}