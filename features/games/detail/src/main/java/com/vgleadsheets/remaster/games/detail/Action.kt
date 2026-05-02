package com.vgleadsheets.remaster.games.detail

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class ComposerClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
    data object AddFavoriteClicked : Action()
    data object RemoveFavoriteClicked : Action()
    data object EnableOfflineClicked : Action()
    data object DisableOfflineClicked : Action()
}
