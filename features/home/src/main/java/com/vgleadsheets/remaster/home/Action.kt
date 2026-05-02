package com.vgleadsheets.remaster.home

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class MostSongsGameClicked(val gameId: Long) : Action()
    data class MostSongsComposerClicked(val composerId: Long) : SageAction()
    data class MostPlaysGameClicked(val gameId: Long) : SageAction()
    data class MostPlaysComposerClicked(val composerId: Long) : SageAction()
    data class MostPlaysSongClicked(val songId: Long) : SageAction()
    data class MostPlaysTagValueClicked(val tagValueId: Long) : SageAction()
    data class RecentSongClicked(val songId: Long) : SageAction()
    data object RandomSongClicked : SageAction()
    data object RandomGameClicked : SageAction()
    data object RandomComposerClicked : SageAction()
}
