package com.vgleadsheets.logging

/**
 * A Hatchet that doesn't do anything. Use in production.
 */
class BluntHatchet : Hatchet {
    override fun v(message: String) = Unit

    override fun d(message: String) = Unit

    override fun i(message: String) = Unit

    override fun w(message: String) = Unit

    override fun e(message: String) = Unit

    override fun log(severity: Int, message: String) = Unit
}
