package com.vgleadsheets.search

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class SearchHistoryEntryClicked(val query: String) : Action()
    data class SearchHistoryEntryRemoveClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
}
