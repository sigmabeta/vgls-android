package com.vgleadsheets.remaster.songs.detail

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class SongThumbnailClicked(val id: Long, val pageNumber: Int) : Action()
    data class TagValueClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
    data object AddFavoriteClicked : Action()
    data object RemoveFavoriteClicked : Action()
    data object EnableOfflineClicked : Action()
    data object DisableOfflineClicked : Action()
    data object ToggleAltSelectedClicked : Action()
    data object SearchYoutubeClicked : Action()
}
