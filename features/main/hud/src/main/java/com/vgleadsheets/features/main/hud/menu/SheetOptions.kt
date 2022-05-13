package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfTracker

object SheetOptions {
    @Suppress("LongParameterList")
    fun getListModels(
        shouldShow: Boolean,
        currentSong: Song?,
        perfTracker: PerfTracker,
        clickHandler: () -> Unit,
        otherClickHandler: () -> Unit,
        resources: Resources,
    ) = if (currentSong != null && shouldShow) {
        listOf(
            MenuItemListModel(
                resources.getString(R.string.label_song_details),
                "",
                R.drawable.ic_details_24,
                clickHandler,
                "",
                perfTracker
            ),
            MenuItemListModel(
                resources.getString(R.string.label_youtube),
                "",
                R.drawable.ic_play_circle_filled_24,
                otherClickHandler,
                "",
                perfTracker
            )
        )
    } else {
        emptyList()
    }
}
