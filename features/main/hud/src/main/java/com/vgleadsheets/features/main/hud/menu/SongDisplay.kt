package com.vgleadsheets.features.main.hud.menu

import com.vgleadsheets.components.IconNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.model.Song

object SongDisplay {
    fun getListModels(
        hudMode: HudMode,
        currentSong: Song?,
        onClick: () -> Unit
    ): List<ListModel> =
        if (currentSong != null && hudMode != HudMode.SEARCH) {
            listOf(
                IconNameCaptionListModel(
                    currentSong.id,
                    currentSong.name,
                    currentSong.gameName,
                    com.vgleadsheets.vectors.R.drawable.ic_baseline_music_note_24,
                    onClick
                ),
            )
        } else {
        emptyList()
    }
}
