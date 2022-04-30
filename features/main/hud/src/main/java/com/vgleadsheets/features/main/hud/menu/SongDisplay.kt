package com.vgleadsheets.features.main.hud.menu

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfTracker

object SongDisplay {
    fun getListModels(
        currentSong: Song?,
        perfTracker: PerfTracker
    ) = if (currentSong != null) {
        listOf(
            MenuItemListModel(
                currentSong.name,
                currentSong.gameName,
                R.drawable.ic_baseline_music_note_24,
                { },
                "",
                perfTracker
            ),
        )
    } else {
        emptyList()
    }
}
