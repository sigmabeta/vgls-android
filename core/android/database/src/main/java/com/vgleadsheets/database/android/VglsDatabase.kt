package com.vgleadsheets.database.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.dao.TransactionDao
import com.vgleadsheets.database.android.entity.ComposerAliasEntity
import com.vgleadsheets.database.android.entity.ComposerEntity
import com.vgleadsheets.database.android.entity.GameAliasEntity
import com.vgleadsheets.database.android.entity.GameEntity
import com.vgleadsheets.database.android.entity.SongAliasEntity
import com.vgleadsheets.database.android.entity.SongEntity
import com.vgleadsheets.database.android.entity.TagKeyEntity
import com.vgleadsheets.database.android.entity.TagValueEntity
import com.vgleadsheets.database.android.entity.TimeEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import com.vgleadsheets.database.android.join.SongTagValueJoin

@Database(
    entities = [
        GameEntity::class,
        SongEntity::class,
        ComposerEntity::class,
        SongComposerJoin::class,
        SongTagValueJoin::class,
        SongAliasEntity::class,
        TimeEntity::class,
        GameAliasEntity::class,
        TagKeyEntity::class,
        TagValueEntity::class,
        ComposerAliasEntity::class
    ],
    version = DatabaseVersions.ADDED_SONG_COUNTS,
)
abstract class VglsDatabase : RoomDatabase() {
    abstract fun composerAliasDao(): ComposerAliasRoomDao
    abstract fun composerDao(): ComposerRoomDao
    abstract fun dbStatisticsDao(): DbStatisticsRoomDao
    abstract fun gameAliasDao(): GameAliasRoomDao
    abstract fun gameDao(): GameRoomDao
    abstract fun songDao(): SongRoomDao
    abstract fun songAliasDao(): SongAliasRoomDao
    abstract fun tagKeyDao(): TagKeyRoomDao
    abstract fun tagValueDao(): TagValueRoomDao

    abstract fun transactionDao(): TransactionDao
}
