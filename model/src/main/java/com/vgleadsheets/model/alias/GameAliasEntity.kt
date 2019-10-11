package com.vgleadsheets.model.alias

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType

@Entity(
    tableName = "alias_game",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("gameId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameAliasEntity(
    val gameId: Long,
    val name: String,
    val giantBombId: Long? = null,
    val photoUrl: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    fun toSearchResult() = SearchResult(gameId, SearchResultType.GAME, name, giantBombId, photoUrl)
}
