package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

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
    val photoUrl: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    fun toGame(songs: List<Song>?) = Game(gameId, name, songs, photoUrl)
}
