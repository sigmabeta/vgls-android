package com.vgleadsheets.features.main.sheet

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.model.song.Song

class Clicks(private val router: FragmentRouter) : ListItemClicks {
    fun composer(composerId: Long) {
        router.showSongListForComposer(composerId)
    }

    fun game(gameId: Long) {
        router.showSongListForGame(gameId)
    }

    fun viewSheet(id: Long) {
        router.showSongViewer(id)
    }

    fun searchYoutube(song: Song) {
        TODO("Not yet implemented")
    }

    fun tagValue(id: Long) {
        router.showSongListForTagValue(id)
    }
}
