package com.vgleadsheets.ui

interface StringProvider {
    fun getString(string: StringId): String
    fun getStringOneArg(string: StringId, arg: String): String
}
