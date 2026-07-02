package com.vgleadsheets.ui.licenses

import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.SageState
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val licensePageUrl: String? = null
) : SageState {
    fun title(stringProvider: StringProvider): TitleBarModel = TitleBarModel(
            title = stringProvider.getString(VglsStringId.SCREEN_TITLE_LICENSES),
            shouldShowBack = true,
        )
}
