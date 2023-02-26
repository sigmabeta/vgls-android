package com.vgleadsheets.features.main.hud.menu

import com.vgleadsheets.components.IconNameCaptionListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.Jam

object JamDisplay {
    fun getListModels(
        hudMode: HudMode,
        activeJam: Jam?,
        onClick: () -> Unit
    ) = if (activeJam != null && hudMode != HudMode.SEARCH) {
        listOf(
            IconNameCaptionListModel(
                activeJam.id,
                activeJam.name,
                "${activeJam.currentSong?.gameName} - ${activeJam.currentSong?.name}",
                R.drawable.ic_playlist_play_black_24dp,
                onClick
            ),
        )
    } else {
        emptyList()
    }
}
