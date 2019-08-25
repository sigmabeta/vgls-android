package com.vgleadsheets.model.composer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "composer"
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String) {
    fun toComposer() = Composer(id, name, null)
}