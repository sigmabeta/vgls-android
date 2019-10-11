package com.vgleadsheets.model.alias

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType

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
    val giantBombId: Long? = null,
    val photoUrl: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    fun toSearchResult() = SearchResult(composerId, SearchResultType.COMPOSER, name, giantBombId, photoUrl)
}
