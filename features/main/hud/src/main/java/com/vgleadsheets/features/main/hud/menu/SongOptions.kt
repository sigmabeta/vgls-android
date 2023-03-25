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
        onFavoriteClick: () -> Unit,
        onOfflineClick: () -> Unit,
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
            ),
            MenuItemListModel(
                resources.getString(
                    if (currentSong.isFavorite) {
                        R.string.label_unfavorite
                    } else {
                        R.string.label_favorite
                    }
                ),
                "",
                if (currentSong.isFavorite) {
                    R.drawable.ic_jam_filled
                } else {
                    R.drawable.ic_jam_unfilled
                },
                onFavoriteClick,
            ),
            MenuItemListModel(
                resources.getString(
                    if (currentSong.isAvailableOffline) {
                        R.string.label_remove_offline
                    } else {
                        R.string.label_add_offline
                    }
                ),
                "",
                if (currentSong.isAvailableOffline) {
                    R.drawable.ic_offline_pin_24
                } else {
                    R.drawable.ic_make_offline_24
                },
                onOfflineClick,
            ),
        )
    } else {
        emptyList()
    }

    private fun isApplicableHudModeLol(hudMode: HudMode) =
        hudMode == HudMode.REGULAR ||
            hudMode == HudMode.HIDDEN
}
