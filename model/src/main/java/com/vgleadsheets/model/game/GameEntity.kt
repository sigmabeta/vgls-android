package com.vgleadsheets.model.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.model.song.Song

@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val giantBombId: Long? = null,
    val photoUrl: String? = null
) {
    fun toGame(songs: List<Song>?) = Game(id, name, songs, giantBombId, photoUrl)

    fun toSearchResult() = SearchResult(id, SearchResultType.GAME, name)
}
