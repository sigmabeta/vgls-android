package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.song.Song

object SheetOptions {
        fun getListModels(
        shouldShow: Boolean,
        currentSong: Song?,
        onDetailsClick: () -> Unit,
        onYoutubeClick: () -> Unit,
        resources: Resources,
    ) = if (currentSong != null && shouldShow) {
        listOf(
            MenuItemListModel(
                resources.getString(R.string.label_song_details),
                "",
                R.drawable.ic_details_24,
                onDetailsClick,
            ),
            MenuItemListModel(
                resources.getString(R.string.label_youtube),
                "",
                R.drawable.ic_play_circle_filled_24,
                onYoutubeClick,
            )
        )
    } else {
        emptyList()
    }
}
