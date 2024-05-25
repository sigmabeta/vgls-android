package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongThumbnailClicked(val id: Long) : Action()
    data class TagValueClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
}
