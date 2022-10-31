package com.vgleadsheets.database.android.join

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.vgleadsheets.database.android.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.join.SongComposerJoin.Companion.ROW_FOREIGN_KEY_ONE
import com.vgleadsheets.database.android.join.SongComposerJoin.Companion.ROW_FOREIGN_KEY_TWO
import com.vgleadsheets.database.android.join.SongComposerJoin.Companion.TABLE

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
            entity = ComposerEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY_TWO),
            onDelete = CASCADE
        )
    ]
)
data class SongComposerJoin(
    val songId: Long,
    val composerId: Long
) {
    companion object {
        const val TABLE = "song_composer_join"

        const val ROW_FOREIGN_KEY_ONE = "songId"
        const val ROW_FOREIGN_KEY_TWO = "composerId"
    }
}
