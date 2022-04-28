package com.vgleadsheets.model

import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song

fun List<Song>.filteredForVocals(selectedPartId: String) = filter { song ->
    selectedPartId != Part.VOCAL.apiId || song.hasVocals
}