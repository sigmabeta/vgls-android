package com.vgleadsheets.ui

interface StringProvider {
    fun getString(string: StringId): String
    fun getStringOneArg(string: StringId, arg: String): String
    fun getStringOneInt(string: StringId, arg: Int): String
    fun getStringTwoArgs(string: StringId, first: String, second: String): String
}
