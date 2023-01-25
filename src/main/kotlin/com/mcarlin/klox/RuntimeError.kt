package com.mcarlin.klox

class RuntimeError(val operator: Token, reason: String) : Throwable(reason)
