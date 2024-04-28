package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song

data class State(
    val baseImageUrl: String? = null,
    val selectedPart: Part? = null,
    val game: Game? = null,
    val songs: List<Song> = emptyList(),
    val composers: List<Composer> = emptyList(),
)
