package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.urlinfo.UrlInfo

data class State(
    val title: String? = null,
    val sheetUrlInfo: UrlInfo = UrlInfo(),
    val game: Game? = null,
    val songs: List<Song> = emptyList(),
    val composers: List<Composer> = emptyList(),
)
