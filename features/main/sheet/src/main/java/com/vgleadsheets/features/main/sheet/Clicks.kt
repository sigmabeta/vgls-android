package com.vgleadsheets.features.main.sheet

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
    private val viewModel: SongViewModel,
) : ListItemClicks {
    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }

    fun composer(composerId: Long) {
        navigator.showComposerDetail(composerId)
    }

    fun game(gameId: Long) {
        navigator.showGameDetail(gameId)
    }

    fun viewSheet(id: Long) {
        navigator.showSongViewer(id)
    }

    fun searchYoutube(name: String, gameName: String) {
        navigator.searchYoutube(name, gameName)
    }

    fun tagValue(id: Long) {
        navigator.showSongListForTagValue(id)
    }
}
