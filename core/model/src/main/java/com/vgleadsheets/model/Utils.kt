package com.vgleadsheets.model

import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song

fun List<Song>.filteredForVocals(selectedPartId: String) = filter { song ->
    selectedPartId != Part.VOCAL.apiId || song.hasVocals
}

fun Song.thumbUrl(baseImageUrl: String, selectedPart: Part) = Page.generateImageUrl(
    baseImageUrl,
    selectedPart,
    filename,
    1
)
