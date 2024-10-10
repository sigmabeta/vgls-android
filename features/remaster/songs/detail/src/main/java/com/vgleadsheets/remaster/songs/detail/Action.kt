package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongThumbnailClicked(val id: Long, val pageNumber: Int) : Action()
    data class TagValueClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
    data object AddFavoriteClicked : Action()
    data object RemoveFavoriteClicked : Action()
    data object ToggleAltSelectedClicked : Action()
    data object SearchYoutubeClicked : Action()
}
