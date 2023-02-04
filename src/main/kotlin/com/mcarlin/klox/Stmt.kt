package com.mcarlin.klox

sealed interface Stmt {
  fun <R> accept(visitor: Visitor<R>): R

interface Visitor<R> {
  fun visitBlockStmt(block: Block): R
  fun visitExpressionStmt(expression: Expression): R
  fun visitPrintStmt(print: Print): R
  fun visitVariableStmt(variable: Variable): R
}

class Block(
  val statements: List<Stmt>,
): Stmt {
  override fun <R> accept(visitor: Stmt.Visitor<R>): R {
    return visitor.visitBlockStmt(this)
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

class Variable(
  val name: Token,
  val initializer: Expr?,
): Stmt {
  override fun <R> accept(visitor: Stmt.Visitor<R>): R {
    return visitor.visitVariableStmt(this)
  }
}
}
