package com.vgleadsheets.features.main.hud.search

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.song.Song

data class SearchContentWrapper(
    val songs: List<Song>,
    val composers: List<Composer>,
    val games: List<Game>
)
