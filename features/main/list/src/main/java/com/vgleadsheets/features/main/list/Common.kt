package com.vgleadsheets.features.main.list

object Common {
    fun noop(): () -> Unit = { }

    fun noopError(): (Exception) -> Unit = { }
}
