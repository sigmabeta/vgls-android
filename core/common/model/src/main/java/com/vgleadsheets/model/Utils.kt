package com.vgleadsheets.model

fun List<Song>.filteredForVocals(selectedPartId: String) = filter { song ->
    selectedPartId != Part.VOCAL.apiId || song.hasVocals
}
