package com.vgleadsheets.model.parts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.song.SongEntity

@Entity(
    tableName = "part",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("songId"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class PartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val songId: Long, val part: String
) {
    fun toPart() = Part(id!!, part)
}
