package com.vgleadsheets.strings

import android.content.res.Resources
import net.sigmabeta.sage.ui.StringProvider

class StringResources(
    private val resources: Resources,
) : StringProvider {
    override fun getString(string: net.sigmabeta.sage.ui.StringId) =
        resources.getString((string as StringId).id())

    override fun getStringOneArg(string: net.sigmabeta.sage.ui.StringId, arg: String) =
        resources.getString((string as StringId).id(), arg)

    override fun getStringOneInt(string: net.sigmabeta.sage.ui.StringId, arg: Int) =
        resources.getString((string as StringId).id(), arg)

    override fun getStringTwoArgs(string: net.sigmabeta.sage.ui.StringId, first: String, second: String) =
        resources.getString((string as StringId).id(), first, second)
}
