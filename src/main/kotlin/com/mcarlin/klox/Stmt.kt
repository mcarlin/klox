package com.mcarlin.klox

sealed interface Stmt {
  fun <R> accept(visitor: Visitor<R>): R

interface Visitor<R> {
  fun visitExpressionStmt(expression: Expression): R
  fun visitPrintStmt(print: Print): R
}
}

class Expression(
  val expression: Expr,
): Stmt {
  override fun <R> accept(visitor: Stmt.Visitor<R>): R {
    return visitor.visitExpressionStmt(this)
  }
}

class Print(
  val expression: Expr,
): Stmt {
  override fun <R> accept(visitor: Stmt.Visitor<R>): R {
    return visitor.visitPrintStmt(this)
  }
}
