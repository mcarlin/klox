package com.mcarlin.tool

import java.io.PrintWriter
import java.nio.charset.Charset

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: generate_ast <output_directory>")
        System.exit(64)
    }
    val outputDir = args[0]
    defineAst(outputDir, "Expr", listOf(
        "Assign : Token name, Expr value",
        "Binary : Expr left, Token operator, Expr right",
        "Grouping: Expr expression",
        "Literal: Any? value",
        "Logical : Expr left, Token operator, Expr right",
        "Unary : Token operator, Expr right",
        "Variable : Token name",
    ))
    defineAst(outputDir, "Stmt", listOf(
        "If: Expr condition, Stmt thenBranch, Stmt? elseBranch",
        "Block : List<Stmt> statements",
        "Expression : Expr expression",
        "Print : Expr expression",
        "Variable : Token name, Expr? initializer",
        "While : Expr condition, Stmt body",

    ))
}

fun defineAst(outputDir: String, baseName: String, types: List<String>) {
   val path = "$outputDir/$baseName.kt"

    val writer = PrintWriter(path, Charset.forName("utf-8"))
    writer.use {
        writer.println("package com.mcarlin.klox")
        writer.println("")
        writer.println("sealed interface $baseName {")
        writer.println("  fun <R> accept(visitor: Visitor<R>): R")
        writer.println("")
        defineVisitor(writer, baseName, types)

        types.forEach {
            val className = it.split(":")[0].trim()
            val fields = it.split(":")[1].trim()

            defineType(writer, baseName, className, fields)
        }
        writer.println("}")
    }
}

fun defineVisitor(writer: PrintWriter, baseName: String, types: List<String>) {
    writer.println("interface Visitor<R> {")
    types.forEach {
        val className = it.split(":")[0].trim()
        writer.println("  fun visit${className}$baseName(${baseName.lowercase()}: $className): R")
    }
    writer.println("}")
}

fun defineType(writer: PrintWriter, baseName: String, className: String, fields: String) {
    writer.println("")
    writer.println("class $className(")
    fields.split(", ").forEach {
       val (ty, name) = it.split(" ")
        writer.println("  val $name: $ty,")
    }
    writer.println("): $baseName {")
    writer.println("  override fun <R> accept(visitor: $baseName.Visitor<R>): R {")
    writer.println("    return visitor.visit${className}$baseName(this)")
    writer.println("  }")
    writer.println("}")
}
