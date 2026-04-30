package com.vgleadsheets.ui.licenses

import net.sigmabeta.sage.appcomm.VglsState
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val licensePageUrl: String? = null
) : VglsState {
    fun title(stringProvider: StringProvider): TitleBarModel = TitleBarModel(
            title = stringProvider.getString(StringId.SCREEN_TITLE_LICENSES),
            shouldShowBack = true,
        )
}
