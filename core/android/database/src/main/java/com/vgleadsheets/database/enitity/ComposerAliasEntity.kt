package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song

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
)

fun ComposerAliasEntity.toModel(songs: List<Song>?) = Composer(composerId, name, songs, photoUrl)
