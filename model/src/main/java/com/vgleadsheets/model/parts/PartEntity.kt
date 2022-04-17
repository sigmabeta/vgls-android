package com.vgleadsheets.model.parts

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.song.SongEntity

@Entity(
    tableName = "part",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("songId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PartEntity(
    @PrimaryKey val id: Long,
    val songId: Long,
    val part: String
) {
    fun toPart(pages: List<Page>?) = Part(id, part, pages)
}
