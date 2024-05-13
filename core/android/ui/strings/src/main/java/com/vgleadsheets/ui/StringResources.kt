package com.vgleadsheets.ui

import android.content.res.Resources

class StringResources(
    private val resources: Resources,
): StringProvider {
    override fun getString(string: StringId) = resources.getString(string.id())
}
