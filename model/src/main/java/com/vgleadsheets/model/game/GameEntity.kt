package com.vgleadsheets.model.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.model.song.Song

@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String
) {
    fun toGame(songs: List<Song>?) = Game(id, name, songs)

    fun toSearchResult() = SearchResult(id, SearchResultType.GAME, name)
}
