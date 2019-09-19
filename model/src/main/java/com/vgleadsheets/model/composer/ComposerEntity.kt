package com.vgleadsheets.model.composer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.model.song.Song

@Entity(
    tableName = "composer"
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String
) {
    fun toComposer(songs: List<Song>?) = Composer(id, name, songs)

    fun toSearchResult() = SearchResult(id, SearchResultType.COMPOSER, name)
}
