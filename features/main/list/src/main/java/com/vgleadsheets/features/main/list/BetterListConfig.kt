package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title

interface BetterListConfig<StateType : MvRxState, ClickHandlerType : ListItemClicks> {
    val titleConfig: Title.Config
    val actionsConfig: Actions.Config
    val contentConfig: Content.Config
    val emptyConfig: EmptyState.Config
    val errorConfig: ErrorState.Config
    val loadingConfig: LoadingState.Config

    companion object {
        const val LOADING_ITEMS = 15

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}
