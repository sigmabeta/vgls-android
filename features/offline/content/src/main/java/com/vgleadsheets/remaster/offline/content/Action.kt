package com.vgleadsheets.remaster.offline.content

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class SongClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
}
