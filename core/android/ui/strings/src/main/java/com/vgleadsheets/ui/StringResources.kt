package com.vgleadsheets.ui

import android.content.res.Resources

class StringResources(
    private val resources: Resources,
) : StringProvider {
    override fun getString(string: StringId) = resources.getString(string.id())

    override fun getStringOneArg(string: StringId, arg: String) = resources.getString(string.id(), arg)
}
