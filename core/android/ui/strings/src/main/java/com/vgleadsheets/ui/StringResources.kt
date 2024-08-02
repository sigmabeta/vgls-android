package com.vgleadsheets.ui

import android.content.res.Resources

class StringResources(
    private val resources: Resources,
) : StringProvider {
    override fun getString(string: StringId) = resources.getString(string.id())

    override fun getStringOneArg(string: StringId, arg: String) = resources.getString(string.id(), arg)

    override fun getStringOneInt(string: StringId, arg: Int) = resources.getString(string.id(), arg)

    override fun getStringTwoArgs(string: StringId, first: String, second: String) = resources.getString(
        string.id(),
        first,
        second
    )
}
