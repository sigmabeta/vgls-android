package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.BetterCompositeState

data class SettingState(
    override val contentLoad: SettingContent = SettingContent(
        Uninitialized
    ),
) : BetterCompositeState<SettingContent>
