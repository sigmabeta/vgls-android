package com.vgleadsheets.features.main.settings.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterSettingState(
    override val contentLoad: BetterSettingContent = BetterSettingContent(
        Uninitialized
    ),
) : BetterCompositeState<BetterSettingContent>
