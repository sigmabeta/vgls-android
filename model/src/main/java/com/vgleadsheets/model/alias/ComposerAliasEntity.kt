package com.vgleadsheets.model.alias

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.song.Song

@Entity(
    tableName = "alias_composer",
    foreignKeys = [
        ForeignKey(
            entity = ComposerEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("composerId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ComposerAliasEntity(
    val composerId: Long,
    val name: String,
    val photoUrl: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    fun toComposer(songs: List<Song>?) = Composer(composerId, name, songs, photoUrl)
}
