package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class ComposerClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
    data object AddFavoriteClicked : Action()
    data object RemoveFavoriteClicked : Action()
}
