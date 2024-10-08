package com.vgleadsheets.logging

class BasicHatchet : Hatchet {
    override fun v(message: String) = println("V: $message")

    override fun d(message: String) = println("D: $message")

    override fun i(message: String) = println("I: $message")

    override fun w(message: String) = println("W: $message")

    override fun e(message: String) = println("E: $message")

    override fun log(severity: Int, message: String) = println("${severity.toSeverityCharacter()}: $message")

    @Suppress("MagicNumber")
    private fun Int.toSeverityCharacter() = when (this) {
        2 -> 'V'
        3 -> 'D'
        4 -> 'I'
        5 -> 'W'
        6 -> 'E'
        else -> 'A'
    }
}
