package com.vgleadsheets.logging

/**
 * A Hatchet that doesn't do anything. Use in production.
 */
class BluntHatchet : Hatchet {
    override fun v(tag: String, message: String) = Unit

    override fun d(tag: String, message: String) = Unit

    override fun i(tag: String, message: String) = Unit

    override fun w(tag: String, message: String) = Unit

    override fun e(tag: String, message: String) = Unit

    override fun log(severity: Int, tag: String, message: String) = Unit
}
