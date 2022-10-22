package com.vgleadsheets.database.join

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.vgleadsheets.database.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.enitity.TagValueEntity
import com.vgleadsheets.database.enitity.TagValueEntity.Companion.TABLE
import com.vgleadsheets.database.join.SongTagValueJoin.Companion.ROW_FOREIGN_KEY_ONE
import com.vgleadsheets.database.join.SongTagValueJoin.Companion.ROW_FOREIGN_KEY_TWO

@Entity(
    tableName = TABLE,
    primaryKeys = [ROW_FOREIGN_KEY_ONE, ROW_FOREIGN_KEY_TWO],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY_ONE),
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = TagValueEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY_TWO),
            onDelete = CASCADE
        )
    ]
)
data class SongTagValueJoin(
    val songId: Long,
    val tagValueId: Long
) {
    companion object {
        const val TABLE = "song_tag_value_join"

        const val ROW_FOREIGN_KEY_ONE = "songId"
        const val ROW_FOREIGN_KEY_TWO = "tagValueId"
    }
}

