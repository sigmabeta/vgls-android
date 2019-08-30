package com.vgleadsheets.model.composer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType

@Entity(
    tableName = "composer"
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String
) {
    fun toComposer() = Composer(id, name, null)

    fun toSearchResult() = SearchResult(id, SearchResultType.COMPOSER, name)
}
