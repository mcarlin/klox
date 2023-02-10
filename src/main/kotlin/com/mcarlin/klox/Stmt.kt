package com.mcarlin.klox

sealed interface Stmt {
  fun <R> accept(visitor: Visitor<R>): R

interface Visitor<R> {
  fun visitIfStmt(stmt: If): R
  fun visitBlockStmt(stmt: Block): R
  fun visitExpressionStmt(stmt: Expression): R
  fun visitPrintStmt(stmt: Print): R
  fun visitVariableStmt(stmt: Variable): R
}

class If(
  val condition: Expr,
  val thenBranch: Stmt,
  val elseBranch: Stmt?,
): Stmt {
  override fun <R> accept(visitor: Stmt.Visitor<R>): R {
    return visitor.visitIfStmt(this)
  }
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
