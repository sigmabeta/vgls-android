package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class MostSongsGameClicked(val gameId: Long) : Action()
    data class MostSongsComposerClicked(val composerId: Long) : VglsAction()
    data class MostPlaysGameClicked(val gameId: Long) : VglsAction()
    data class MostPlaysComposerClicked(val composerId: Long) : VglsAction()
    data class MostPlaysSongClicked(val songId: Long) : VglsAction()
    data class MostPlaysTagValueClicked(val tagValueId: Long) : VglsAction()
    data class RecentSongClicked(val songId: Long) : VglsAction()
    data object RandomSongClicked : VglsAction()
    data object RandomGameClicked : VglsAction()
    data object RandomComposerClicked : VglsAction()
}
