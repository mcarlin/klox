package com.mcarlin.klox

import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

val interpreter = Interpreter()
var hadError = false
var hadRuntimeError = false

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: klox [script]")
    } else if (args.size == 1) {
        runFile(args[0])
    } else {
        runPrompt()
    }
}


fun runFile(path: String) {
    val bytes = Files.readAllBytes(Paths.get(path))
    run(bytes.toString(Charset.defaultCharset()))

    if (hadError) {
        exitProcess(65)
    }
    if (hadRuntimeError) {
        exitProcess(70)
    }
}

fun run(source: String) {
    val scanner = Scanner(source)
    val tokens = scanner.scanTokens()
    if (tokens.size == 1 && tokens[0].type == TokenType.EOF) {
        return
    }

    val parser = Parser(tokens)
    val statements = parser.parse()


    if (hadError) return


    if (statements.size == 1) {
        val statement = statements[0]
        if (statement is Stmt.Expression) {
            statements[0] = Stmt.Print(statement.expression)
        }
    }

    interpreter.interpret(statements)
}

fun runPrompt() {
    val reader = InputStreamReader(System.`in`).buffered()

    while (true) {
        print("> ")
        val line = reader.readLine() ?: break
        run(line)
    }
}

fun error(line: Int, message: String) {
    report(line, "", message)
}

fun error(token: Token, message: String) {
    if (token.type == TokenType.EOF) {
        report(token.line, " at end", message)
    } else {
        report(token.line, " at '${token.lexeme}'", message)
    }
}

fun report(line: Int, where: String, message: String) {
    System.err.println("[line $line] Error$where: $message")
    hadError = true
}

fun runtimeError(error: RuntimeError) {
    System.err.println("${error.message}\n[line ${error.operator.line}]")
    hadRuntimeError = true
}