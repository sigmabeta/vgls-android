package com.vgleadsheets.model.song

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.vgleadsheets.model.game.GameEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("game_id"),
            onDelete = CASCADE
        )]
)
data class Song(
    @PrimaryKey val id: Long,
    val filename: String,
    val name: String,
    val pageCount: Int,
    val game_id: Long? = null
)
