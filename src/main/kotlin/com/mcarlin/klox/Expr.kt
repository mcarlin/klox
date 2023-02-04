package com.mcarlin.klox

sealed interface Expr {
  fun <R> accept(visitor: Visitor<R>): R

interface Visitor<R> {
  fun visitAssignExpr(assign: Assign): R
  fun visitBinaryExpr(binary: Binary): R
  fun visitGroupingExpr(grouping: Grouping): R
  fun visitLiteralExpr(literal: Literal): R
  fun visitUnaryExpr(unary: Unary): R
  fun visitVariableExpr(variable: Variable): R
}

class Assign(
  val name: Token,
  val value: Expr,
): Expr {
  override fun <R> accept(visitor: Expr.Visitor<R>): R {
    return visitor.visitAssignExpr(this)
  }
}

class Binary(
  val left: Expr,
  val operator: Token,
  val right: Expr,
): Expr {
  override fun <R> accept(visitor: Expr.Visitor<R>): R {
    return visitor.visitBinaryExpr(this)
  }
}

class Grouping(
  val expression: Expr,
): Expr {
  override fun <R> accept(visitor: Expr.Visitor<R>): R {
    return visitor.visitGroupingExpr(this)
  }
}

class Literal(
  val value: Any?,
): Expr {
  override fun <R> accept(visitor: Expr.Visitor<R>): R {
    return visitor.visitLiteralExpr(this)
  }
}

class Unary(
  val operator: Token,
  val right: Expr,
): Expr {
  override fun <R> accept(visitor: Expr.Visitor<R>): R {
    return visitor.visitUnaryExpr(this)
  }
}

class Variable(
  val name: Token,
): Expr {
  override fun <R> accept(visitor: Expr.Visitor<R>): R {
    return visitor.visitVariableExpr(this)
  }
}
}
