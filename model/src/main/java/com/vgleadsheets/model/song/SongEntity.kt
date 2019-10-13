package com.vgleadsheets.model.song

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = "song",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("game_id"),
            onDelete = CASCADE
        )]
)
data class SongEntity(
    @PrimaryKey val id: Long,
    val filename: String,
    val name: String,
    val pageCount: Int,
    val game_id: Long
) {
    fun toSong(
        composers: List<Composer>?,
        parts: List<Part>?
    ) = Song(
        id,
        filename,
        name,
        composers,
        parts
    )

    fun toSearchResult() = SearchResult(id, SearchResultType.SONG, name, null, null)
}
