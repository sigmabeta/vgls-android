package com.vgleadsheets.features.main.hud.menu

import com.vgleadsheets.components.ComposableModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.model.Song

object SongDisplay {
    fun getListModels(
        hudMode: HudMode,
        currentSong: Song?,
        viewerScreenVisible: Boolean,
        onClick: () -> Unit
    ): List<ComposableModel> = if (currentSong != null && hudMode != HudMode.SEARCH && viewerScreenVisible) {
        listOf(
//            IconNameCaptionListModel(
//                currentSong.id,
//                currentSong.name,
//                currentSong.gameName,
//                R.drawable.ic_baseline_music_note_24,
//                onClick
//            ),
        )
    } else {
        emptyList()
    }
}
