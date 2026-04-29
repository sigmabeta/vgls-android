package com.vgleadsheets.ui.licenses

import net.sigmabeta.sage.appcomm.VglsState
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val licensePageUrl: String? = null
) : VglsState {
    fun title(stringProvider: StringProvider): TitleBarModel = TitleBarModel(
            title = stringProvider.getString(StringId.SCREEN_TITLE_LICENSES),
            shouldShowBack = true,
        )
}
