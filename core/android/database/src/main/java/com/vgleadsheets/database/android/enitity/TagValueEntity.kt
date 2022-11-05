package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.TagValueEntity.Companion.ROW_FOREIGN_KEY
import com.vgleadsheets.database.android.enitity.TagValueEntity.Companion.TABLE

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = TagKeyEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TagValueEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val tag_key_id: Long,
    val tag_key_name: String
) {
    companion object {
        const val TABLE = "tag_value"

        const val ROW_FOREIGN_KEY = "tag_key_id"
    }
}
