package com.vgleadsheets.model.pages

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.parts.PartEntity

@Entity(
    tableName = "page",
    foreignKeys = [
        ForeignKey(
            entity = PartEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("partId"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class PageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val number: Int,
    val partId: Long,
    val imageUrl: String
) {
    fun toPage() = Page(number, imageUrl)
}
