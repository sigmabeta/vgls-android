package com.vgleadsheets.resources

interface ResourceProvider {
    fun getString(id: Int, vararg formatArgs: Any?): String
}
