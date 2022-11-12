package com.mcarlin.klox

import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

var hadError = false

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
}

fun run(source: String) {
    val scanner = Scanner(source)
    val tokens = scanner.scanTokens()
    val parser = Parser(tokens)
    val expression = parser.parse()

    if (hadError) return

    if (expression != null) {
        println(AstPrinter().print(expression))
    }
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
