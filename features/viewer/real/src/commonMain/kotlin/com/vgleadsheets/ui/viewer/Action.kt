package com.vgleadsheets.ui.viewer

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data object ScreenClicked : Action()
    data object PrevButtonClicked : Action()
    data object NextButtonClicked : Action()
    data object LeftArrowPressed : Action()
    data object RightArrowPressed : Action()
}
