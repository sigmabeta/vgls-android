package com.vgleadsheets.model.song

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.GameEntity

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = "song",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("game_id"),
            onDelete = CASCADE
        )
    ]
)
data class SongEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val filename: String,
    val pageCount: Int,
    val lyricPageCount: Int,
    val gameName: String,
    val game_id: Long,
    val playCount: Int = 0
) {
    fun toSong(
        composers: List<Composer>?
    ) = Song(
        id,
        name,
        filename,
        game_id,
        gameName,
        lyricPageCount > 0,
        pageCount,
        lyricPageCount,
        composers,
        playCount
    )
}
