package com.vgleadsheets.features.main.sheet

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val viewModel: SongViewModel,
) : ListItemClicks {
    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }

    fun composer(composerId: Long) {
        router.showSongListForComposer(composerId)
    }

    fun game(gameId: Long) {
        router.showGameDetail(gameId)
    }

    fun viewSheet(id: Long) {
        router.showSongViewer(id)
    }

    fun searchYoutube(name: String, gameName: String) {
        router.searchYoutube(name, gameName)
    }

    fun tagValue(id: Long) {
        router.showSongListForTagValue(id)
    }
}
