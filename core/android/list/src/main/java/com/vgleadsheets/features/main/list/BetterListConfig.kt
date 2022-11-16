package com.vgleadsheets.features.main.list

import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title

interface BetterListConfig {
    val titleConfig: Title.Config
    val actionsConfig: Actions.Config
    val contentConfig: Content.Config
    val emptyConfig: EmptyState.Config
    val errorConfig: ErrorState.Config
    val loadingConfig: LoadingState.Config

    companion object {
        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}
