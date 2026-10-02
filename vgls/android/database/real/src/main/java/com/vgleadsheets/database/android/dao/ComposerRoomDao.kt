package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.dao.RoomDao.Companion.DELETE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.INNER_JOIN
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ON
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_ALPHABETICAL_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_CASE_INSENSITIVE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_LIMIT
import com.vgleadsheets.database.android.dao.RoomDao.Companion.OPTION_SONG_COUNT_ORDER
import com.vgleadsheets.database.android.dao.RoomDao.Companion.SET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.TOGGLE_OFFLINE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.UPDATE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_FAVORITE
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SEARCH
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.join.SongComposerJoin
import kotlinx.coroutines.flow.Flow

@Dao
@Suppress("TooManyFunctions")
interface ComposerRoomDao : RoomDao<ComposerEntity> {
    @Query(QUERY_SEARCH)
    fun searchByName(name: String): Flow<List<ComposerEntity>>

    @Query(QUERY_FOR_SONG)
    fun getForSong(id: Long): Flow<List<ComposerEntity>>

    @Query(QUERY_FOR_SONG)
    suspend fun getForSongSync(id: Long): List<ComposerEntity>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<ComposerEntity>

    @Query(QUERY_SINGLE)
    override suspend fun getOneByIdSync(id: Long): ComposerEntity

    @Query(QUERY_IDS)
    fun getByIdList(ids: Array<Long>): Flow<List<ComposerEntity>>

    @Query(QUERY_ALL)
    override fun getAll(): Flow<List<ComposerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entities: List<ComposerEntity>)

    @Delete(entity = ComposerEntity::class)
    override suspend fun remove(ids: List<DeletionId>)

    @Query(QUERY_MOST_SONGS)
    fun getMostSongsComposers(): Flow<List<ComposerEntity>>

    @Query(QUERY_FAVORITES)
    fun getFavorites(): Flow<List<ComposerEntity>>

    @Query(QUERY_INCREMENT)
    suspend fun incrementSheetsPlayed(id: Long)

    @Query(QUERY_TOGGLE_FAVORITE)
    suspend fun toggleFavorite(id: Long)

    @Query(QUERY_TOGGLE_OFFLINE)
    suspend fun toggleOffline(id: Long)

    @Insert
    suspend fun insertJoins(joins: List<SongComposerJoin>)

    @Query(QUERY_GAME_COVERS)
    fun getGameCovers(): Flow<List<ComposerGameCover>>

    @Query(QUERY_GAME_COVERS)
    suspend fun getGameCoversSync(): List<ComposerGameCover>

    @Query(QUERY_HIGHEST_ID)
    fun getHighestId(): Flow<ComposerEntity>

    @Query(QUERY_DELETE)
    override suspend fun nukeTable()

    companion object {

        // Query Properties

        private const val TABLE = ComposerEntity.TABLE
        private const val TABLE_SONG = SongComposerJoin.TABLE
        private const val TABLE_SONGS = SongEntity.TABLE
        private const val TABLE_GAMES = GameEntity.TABLE

        private const val COLUMN_FOREIGN_KEY_SONG = SongComposerJoin.COLUMN_FOREIGN_KEY_ONE
        private const val COLUMN_FOREIGN_KEY_SONG_JOIN = SongComposerJoin.COLUMN_FOREIGN_KEY_TWO

        private const val OPTION_BY_ID = "ORDER BY id DESC"
        private const val OPTION_NUM_RECORDS_BY_ID = "$OPTION_LIMIT 1"
        private const val OPTION_NUM_RECORDS_MOST = "$OPTION_LIMIT 40"

        private const val COLUMN_INCREMENTABLE = "sheetsPlayed"

        private const val WHERE_IDS = "$WHERE id in (:ids)"
        private const val SET_INCREMENT = "$SET $COLUMN_INCREMENTABLE = $COLUMN_INCREMENTABLE + 1"

        private const val WHERE_SONG =
            "$INNER_JOIN $TABLE_SONG $ON $TABLE_SONG.$COLUMN_FOREIGN_KEY_SONG_JOIN = $COLUMN_PRIMARY_KEY_ID" +
                " $WHERE $COLUMN_FOREIGN_KEY_SONG = :$COLUMN_PRIMARY_KEY_ID"

        // Bespoke Queries

        private const val QUERY_FOR_SONG = "$GET $TABLE $WHERE_SONG $OPTION_ALPHABETICAL_ORDER"

        // Every (composer, game with art) pair, each composer's games ordered by how many of the
        // composer's songs they hold.
        private const val QUERY_GAME_COVERS =
            "SELECT $TABLE_SONG.$COLUMN_FOREIGN_KEY_SONG_JOIN AS composerId," +
                " $TABLE_GAMES.photoUrl AS photoUrl, $TABLE_GAMES.igdbImageId AS igdbImageId" +
                " FROM $TABLE_SONG" +
                " $INNER_JOIN $TABLE_SONGS" +
                " $ON $TABLE_SONGS.$COLUMN_PRIMARY_KEY_ID = $TABLE_SONG.$COLUMN_FOREIGN_KEY_SONG" +
                " $INNER_JOIN $TABLE_GAMES" +
                " $ON $TABLE_GAMES.$COLUMN_PRIMARY_KEY_ID = $TABLE_SONGS.${GameEntity.COLUMN_FOREIGN_KEY}" +
                " $WHERE $TABLE_GAMES.photoUrl IS NOT NULL OR $TABLE_GAMES.igdbImageId IS NOT NULL" +
                " GROUP BY $TABLE_SONG.$COLUMN_FOREIGN_KEY_SONG_JOIN, $TABLE_GAMES.$COLUMN_PRIMARY_KEY_ID" +
                " ORDER BY $TABLE_SONG.$COLUMN_FOREIGN_KEY_SONG_JOIN, COUNT(*) DESC, $TABLE_GAMES.name"

        // Default Queries

        private const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        private const val QUERY_ALL = "$GET $TABLE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        private const val QUERY_IDS = "$GET $TABLE $WHERE_IDS"

        private const val QUERY_SEARCH =
            "$GET $TABLE $WHERE_SEARCH $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        private const val QUERY_DELETE = "$DELETE $TABLE"
        private const val QUERY_UPDATE = "$UPDATE $TABLE"
        private const val QUERY_FAVORITES =
            "$GET $TABLE $WHERE_FAVORITE $OPTION_ALPHABETICAL_ORDER $OPTION_CASE_INSENSITIVE"
        private const val QUERY_MOST_SONGS = "$GET $TABLE $OPTION_SONG_COUNT_ORDER $OPTION_NUM_RECORDS_MOST"

        private const val QUERY_INCREMENT = "$QUERY_UPDATE $SET_INCREMENT $WHERE_SINGLE"

        private const val QUERY_TOGGLE_FAVORITE = "$QUERY_UPDATE $TOGGLE_FAVORITE $WHERE_SINGLE"
        private const val QUERY_TOGGLE_OFFLINE = "$QUERY_UPDATE $TOGGLE_OFFLINE $WHERE_SINGLE"
        private const val QUERY_HIGHEST_ID = "$GET $TABLE $OPTION_BY_ID $OPTION_NUM_RECORDS_BY_ID"
    }
}
