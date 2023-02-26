package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.IconNameCaptionListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.Jam

object JamDisplay {
    fun getListModels(
        hudMode: HudMode,
        activeJam: Jam?,
        onNameClick: () -> Unit,
        onCurrentSongClick: () -> Unit,
        onUnfollowClick: () -> Unit,
        resources: Resources,
    ) = if (activeJam != null && hudMode != HudMode.SEARCH) {
        val currentSong = activeJam.currentSong
        val caption = if (currentSong != null) {
            "${currentSong.gameName} - ${currentSong.name}"
        } else {
            resources.getString(R.string.label_jam_no_song)
        }

        val title = resources.getString(R.string.label_jam_name, activeJam.name)

        listOf(
            IconNameCaptionListModel(
                activeJam.id,
                title,
                caption,
                R.drawable.ic_playlist_play_black_24dp,
                onNameClick
            ),
            MenuItemListModel(
                resources.getString(R.string.label_jam_current_song),
                "",
                R.drawable.ic_description_24dp,
                onCurrentSongClick,
            ),
            MenuItemListModel(
                resources.getString(R.string.label_jam_unfollow),
                "",
                R.drawable.ic_clear_black_24dp,
                onUnfollowClick,
            )
        )
    } else {
        emptyList()
    }
}
