package com.mcarlin.klox

class Environment(
    private val values: MutableMap<String, Any?> = mutableMapOf(),
    private val enclosing: Environment? = null,
) {
    fun define(name: String, value: Any?) {
        values[name] = value
    }


    fun assign(name: Token, value: Any?) {
        if (values.containsKey(name.lexeme)) {
            values[name.lexeme] = value
            return;
        }
        if (enclosing != null) {
            return enclosing.assign(name, value)
        }

        throw RuntimeError(name, "Undefined variable: '${name.lexeme}'.")
    }

    fun get(name: Token): Any? {
        if (values.containsKey(name.lexeme)) {
           return values[name.lexeme]
        }

        if (enclosing != null) {
            return enclosing.get(name)
        }

        throw RuntimeError(name, "Undefined variable '${name.lexeme}'.")
    }
}