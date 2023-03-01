package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.Song

object SongOptions {
    fun getListModels(
        hudMode: HudMode,
        currentSong: Song?,
        onDetailsClick: () -> Unit,
        onYoutubeClick: () -> Unit,
        resources: Resources,
    ) = if (currentSong != null && isApplicableHudModeLol(hudMode)) {
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

    private fun isApplicableHudModeLol(hudMode: HudMode) =
        hudMode == HudMode.REGULAR ||
            hudMode == HudMode.HIDDEN
}
