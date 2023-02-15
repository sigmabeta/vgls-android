package com.vgleadsheets.logging

class BasicHatchet: Hatchet {
    override fun v(tag: String, message: String) = println("V: $tag | $message")

    override fun d(tag: String, message: String) = println("D: $tag | $message")

    override fun i(tag: String, message: String) = println("I: $tag | $message")

    override fun w(tag: String, message: String) = println("W: $tag | $message")

    override fun e(tag: String, message: String) = println("E: $tag | $message")

    override fun log(severity: Int, tag: String, message: String) = println("$severity: $tag | $message")
}
