package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.Song

object SheetOptions {
    fun getListModels(
        hudMode: HudMode,
        currentSong: Song?,
        viewerScreenVisible: Boolean,
        onDetailsClick: () -> Unit,
        onYoutubeClick: () -> Unit,
        resources: Resources,
    ): List<ListModel> =
        if (currentSong != null && isApplicableHudModeLol(hudMode) && viewerScreenVisible) {
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
