package com.vgleadsheets.logging

interface Hatchet {
    fun v(message: String)

    fun d(message: String)

    fun i(message: String)

    fun w(message: String)

    fun e(message: String)
    fun log(severity: Int, message: String)
}
