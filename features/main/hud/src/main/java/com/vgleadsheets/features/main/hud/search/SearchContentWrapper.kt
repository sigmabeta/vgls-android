package com.vgleadsheets.features.main.hud.search

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

data class SearchContentWrapper(
    val songs: List<Song>,
    val composers: List<Composer>,
    val games: List<Game>
)
