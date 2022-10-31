package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity.Companion.ROW_FOREIGN_KEY
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity.Companion.TABLE

@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = ComposerEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY),
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
    companion object {
        const val TABLE = "alias_composer"

        const val ROW_FOREIGN_KEY = "composerId"
    }
}
