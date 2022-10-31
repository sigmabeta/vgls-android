package com.vgleadsheets.repository

import com.vgleadsheets.coroutines.CustomFlows
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.JamDataSource
import com.vgleadsheets.database.dao.SetlistEntryDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.SongHistoryEntryDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.VglsApiGame
import com.vgleadsheets.tracking.Tracker
import java.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration

class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val threeTen: ThreeTenTime,
    private val tracker: Tracker,
    private val dispatchers: VglsDispatchers,
    private val composerAliasDataSource: ComposerAliasDataSource,
    private val composerDataSource: ComposerDataSource,
    private val dbStatisticsDataSource: DbStatisticsDataSource,
    private val gameAliasDataSource: GameAliasDataSource,
    private val gameDataSource: GameDataSource,
    private val jamDataSource: JamDataSource,
    private val setlistEntryDataSource: SetlistEntryDataSource,
    private val songDataSource: SongDataSource,
    private val songHistoryEntryDataSource: SongHistoryEntryDataSource,
    private val tagKeyDataSource: TagKeyDataSource,
    private val tagValueDataSource: TagValueDataSource
) : Repository {
    @Suppress("ReturnCount")
    override suspend fun checkShouldAutoUpdate(): Boolean {
        val lastCheckTime = withContext(dispatchers.disk) {
            getLastCheckTime()
        }

        if (threeTen.now().toInstant().toEpochMilli() - lastCheckTime.time_ms <= AGE_THRESHOLD) {
            return false
        }

        val lastApiUpdateTime = withContext(dispatchers.network) { getLastApiUpdateTime() }
        val lastDbUpdateTime = withContext(dispatchers.disk) { getLastDbUpdateTimeOnce() }

        val diff = lastApiUpdateTime.timeMs - lastDbUpdateTime.timeMs
        if (diff < 0L) {
            return false
        }

        tracker.logAutoRefresh()
        return true
    }

    override suspend fun refresh() = getDigest()

    override fun observeJamState(id: Long) = jamDataSource
        .getOneById(id)

    override fun refreshJamStateContinuously(name: String) =
        CustomFlows.emitOnInterval(INTERVAL_JAM_REFRESH) {
            refreshJamStateImpl(name)
        }

    override suspend fun refreshJamState(name: String) = refreshJamStateImpl(name)

    override suspend fun refreshSetlist(jamId: Long, name: String) = refreshSetlistImpl(jamId, name)

    override fun getGames(withSongs: Boolean): Flow<List<Game>> = gameDataSource.getAll(withSongs)
        .flowOn(dispatchers.disk)

    override fun getSong(
        songId: Long,
        withComposers: Boolean
    ): Flow<Song> {
        val flow = songDataSource
            .get(songId)
        return flow
            .flowOn(dispatchers.disk)
    }

    override fun getAllSongs(withComposers: Boolean) = songDao
        .getAll()
        .map { songEntities ->
            songEntities.map { songEntity ->
                val composers = if (withComposers) getComposersForSong(songEntity) else null
                songEntity.toSong(composers)
            }
        }
        .flowOn(dispatchers.disk)

    override fun getComposers(withSongs: Boolean): Flow<List<Composer>> = composerDao
        .getAll()
        .map { composerEntities ->
            composerEntities.map { composerEntity ->
                val songs = getSongsForComposer(composerEntity)
                composerEntity.toComposer(songs)
            }
        }
        .flowOn(dispatchers.disk)

    override fun getAllTagKeys(withValues: Boolean) = tagKeyDao
        .getAll()
        .map { tagKeyEntities ->
            tagKeyEntities.map {
                val values = if (withValues) getTagValuesForTagKeySync(it, false) else null
                it.toTagKey(values)
            }
        }
        .flowOn(dispatchers.disk)

    override fun getJam(id: Long, withHistory: Boolean) = jamDao
        .getJam(id)
        .map {
            val currentSong = it.getCurrentSong()
            val songHistory = if (withHistory) getSongHistoryForJamSync(id) else null

            it.toJam(currentSong, songHistory)
        }
        .flowOn(dispatchers.disk)

    override fun getJams() = jamDao
        .getAll()
        .map {
            it.map { jamEntity ->
                val currentSong = jamEntity.getCurrentSong()
                jamEntity.toJam(currentSong, null)
            }
        }
        .flowOn(dispatchers.disk)

    override fun getComposer(composerId: Long): Flow<Composer> = composerDao
        .getComposer(composerId)
        .map { it.toComposer(null) }
        .flowOn(dispatchers.disk)

    override fun getGame(gameId: Long): Flow<Game> = gameDao
        .getGame(gameId)
        .map { it.toGame(null) }
        .flowOn(dispatchers.disk)

    override fun getTagKey(tagKeyId: Long) = tagKeyDao
        .getTagKey(tagKeyId)
        .map { it.toTagKey(null) }
        .flowOn(dispatchers.disk)

    override fun getTagValue(tagValueId: Long) = tagValueDao
        .getTagValue(tagValueId)
        .map { it.toTagValue(null) }
        .flowOn(dispatchers.disk)

    @Suppress("MaxLineLength")
    override fun searchSongs(searchQuery: String) = songDao
        .searchSongsByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { songEntities ->
            songEntities.map {
                val composers = getComposersForSong(it)
                it.toSong(composers)
            }
        }
        .flowOn(dispatchers.disk)

    override fun searchGamesCombined(searchQuery: String) = searchGames(searchQuery).combine(
        searchGameAliases(searchQuery)
    ) { games: List<Game>, gameAliases: List<Game> ->
        games + gameAliases
    }.map { games ->
        games.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    override fun searchComposersCombined(searchQuery: String) =
        searchComposers(searchQuery).combine(
            searchComposerAliases(searchQuery)
        ) { composers: List<Composer>, composerAliases: List<Composer> ->
            composers + composerAliases
        }.map { composers ->
            composers.distinctBy { it.id }
        }.flowOn(dispatchers.disk)

    override fun getLastUpdateTime(): Flow<Time> = getLastDbUpdateTime()

    override suspend fun removeJam(id: Long) = withContext(dispatchers.disk) {
        songHistoryEntryDao.removeAllForJam(id)
        setlistEntryDao.removeAllForJam(id)
        jamDao.remove(id)
    }

    override suspend fun refreshJams() = withContext(dispatchers.network) {
        val jams = jamDao.getAll()
            .filter { it.isNotEmpty() }
            .firstOrNull()

        jams?.forEach {
            refreshJamStateImpl(it.name)
        }

        return@withContext
    }

    override suspend fun clearSheets() = withContext(dispatchers.disk) {
        gameDao.nukeTable()
        songDao.nukeTable()
        composerDao.nukeTable()
        songComposerDao.nukeTable()
        tagKeyDao.nukeTable()
        tagValueDao.nukeTable()
        songTagValueDao.nukeTable()
        gameAliasDao.nukeTable()
        composerAliasDao.nukeTable()
        dbStatisticsDao.nukeTable()
    }

    override suspend fun clearJams() = withContext(dispatchers.disk) {
        songHistoryEntryDao.nukeTable()
        setlistEntryDao.nukeTable()
        jamDao.nukeTable()
    }

    @Suppress("MaxLineLength")
    private fun searchGames(searchQuery: String) = gameDao
        .searchGamesByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { gameEntities ->
            gameEntities.map {
                val songs = getSongsForGameEntity(it)
                it.toGame(songs)
            }
        }

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String) = composerDao
        .searchComposersByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { composerEntities ->
            composerEntities.map {
                val songs = getSongsForComposer(it)
                it.toComposer(songs)
            }
        }

    @Suppress("MaxLineLength")
    private fun searchGameAliases(searchQuery: String) = gameAliasDao
        .getAliasesByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { aliasEntities ->
            aliasEntities.map {
                val songs = getSongsForGameAlias(it)
                it.toGame(songs)
            }
        }

    @Suppress("MaxLineLength")
    private fun searchComposerAliases(searchQuery: String) = composerAliasDao
        .getAliasesByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { aliasEntities ->
            aliasEntities.map {
                val songs = getSongsForComposerAlias(it)
                it.toComposer(
                    songs
                )
            }
        }

    private fun getComposersForSong(songEntity: SongEntity) =
        songComposerDao.getComposersForSong(songEntity.id)
            .map { composerEntity -> composerEntity.toComposer(null) }

    private fun getSongsForGameEntity(gameEntity: GameEntity) =
        songDao.getSongsForGameSync(gameEntity.id)
            .map { songEntity ->
                songEntity.toSong(null)
            }

    private fun getSongsForGameAlias(gameAliasEntity: GameAliasEntity) =
        songDao.getSongsForGameSync(gameAliasEntity.gameId)
            .map { songEntity ->
                songEntity.toSong(null)
            }

    private fun getSongsForComposer(composerEntity: ComposerEntity) =
        songComposerDao.getSongsForComposerSync(composerEntity.id)
            .map { songEntity ->
                songEntity.toSong(null)
            }

    private fun getTagValuesForTagKeySync(tagKeyEntity: TagKeyEntity, withSongs: Boolean = true) =
        tagValueDao.getValuesForTagSync(tagKeyEntity.id)
            .map { tagValueEntity ->
                val songs = if (withSongs) getSongsForTagValueSync(tagValueEntity) else null
                tagValueEntity.toTagValue(songs)
            }

    private fun getSongsForTagValueSync(tagValueEntity: TagValueEntity) =
        songTagValueDao
            .getSongsForTagValueSync(tagValueEntity.id)
            .map { songEntity ->
                songEntity.toSong(null)
            }

    private fun getSongsForComposerAlias(
        composerAliasEntity: ComposerAliasEntity,
    ) = songComposerDao.getSongsForComposerSync(composerAliasEntity.composerId)
        .map { songEntity ->
            songEntity.toSong(null)
        }

    private fun getSongHistoryForJamSync(jamId: Long) = songHistoryEntryDao
        .getSongHistoryEntriesForJamSync(jamId)
        .map { songHistoryEntryEntity ->
            val song = getSongSync(songHistoryEntryEntity.song_id)?.toSong(null)

            songHistoryEntryEntity.toSongHistoryEntry(song)
        }

    private fun getSongSync(songId: Long) = songDao.getSongSync(songId)

    private suspend fun refreshJamStateImpl(name: String) {
        val jam = withContext(dispatchers.network) {
            vglsApi.getJamState(name)
        }

        val songHistory = jam.song_history

        val songHistoryEntries = if (songHistory.isNotEmpty()) {
            songHistory
                .drop(1)
                .mapIndexed { songHistoryIndex, songHistoryEntry ->
                    songHistoryEntry.toEntity(jam.jam_id, songHistoryIndex)
                }
        } else {
            emptyList()
        }

        val jamEntity = jam.toEntity(name.lowercase())

        withContext(dispatchers.disk) {
            jamDao.upsertJam(songHistoryEntryDao, jamEntity, songHistoryEntries)
        }
    }

    private suspend fun refreshSetlistImpl(jamId: Long, name: String) {
        val setlist = vglsApi.getSetlistForJam(name)

        val setlistEntries = setlist.songs.mapIndexed() { setlistIndex, entry ->
            entry.toEntity(jamId, setlistIndex)
        }

        withContext(dispatchers.disk) {
            setlistEntryDao.removeAllForJam(jamId)
            setlistEntryDao.insertAll(setlistEntries)
        }
    }

    private suspend fun getLastCheckTime() = dbStatisticsDao
        .getTime(TimeType.LAST_CHECKED.ordinal)
        .map { it.firstOrNull() ?: TimeEntity(TimeType.LAST_CHECKED.ordinal, 0L) }
        .first()

    private fun getLastDbUpdateTime() = dbStatisticsDao
        .getTime(TimeType.LAST_UPDATED.ordinal)
        .map { it.firstOrNull() ?: TimeEntity(TimeType.LAST_UPDATED.ordinal, 0L) }
        .map {
            it.toTime()
        }

    private suspend fun getLastDbUpdateTimeOnce() = getLastDbUpdateTime().first()

    private suspend fun getLastApiUpdateTime(): Time {
        val lastUpdate = vglsApi.getLastUpdateTime()
        val time = lastUpdate.toEntity().toTime()

        dbStatisticsDao.insert(
            TimeEntity(TimeType.LAST_UPDATED.ordinal, time.timeMs)
        )

        return time
    }

    @Suppress("DefaultLocale", "LongMethod", "ComplexMethod")
    private suspend fun getDigest() {
        val digest = withContext(dispatchers.network) {
            vglsApi.getDigest()
        }

        val apiComposers = digest.composers
        val apiGames = digest.games

        val composerEntities = createComposerMap(apiComposers)

        val gameEntities = apiGames.map { apiGame -> apiGame.toEntity() }

        val songEntities = ArrayList<SongEntity>(CAPACITY)
        val songComposerJoins = ArrayList<SongComposerJoin>(CAPACITY)
        val songTagValueJoins = ArrayList<SongTagValueJoin>(CAPACITY)

        val tagKeyEntities = HashMap<String, TagKeyEntity>(CAPACITY)
        val tagValueEntities = HashMap<String, TagValueEntity>(CAPACITY)

        apiGames.forEach { apiGame ->
            processGame(
                apiGame,
                composerEntities,
                songComposerJoins,
                tagKeyEntities,
                tagValueEntities,
                songTagValueJoins,
                songEntities
            )
        }

        withContext(dispatchers.disk) {
            gameDao.refreshTable(
                gameEntities,
                songDao,
                composerDao,
                songComposerDao,
                songTagValueDao,
                tagKeyDao,
                tagValueDao,
                songEntities,
                composerEntities.values.toList(),
                songComposerJoins,
                songTagValueJoins,
                tagKeyEntities.values.toList(),
                tagValueEntities.values.toList()
            )

            dbStatisticsDao.insert(
                TimeEntity(
                    TimeType.LAST_CHECKED.ordinal,
                    threeTen.now().toInstant().toEpochMilli()
                )
            )
        }
    }

    private fun processGame(
        apiGame: VglsApiGame,
        composerEntities: MutableMap<Long, ComposerEntity>,
        songComposerJoins: ArrayList<SongComposerJoin>,
        tagKeyEntities: HashMap<String, TagKeyEntity>,
        tagValueEntities: HashMap<String, TagValueEntity>,
        songTagValueJoins: ArrayList<SongTagValueJoin>,
        songEntities: ArrayList<SongEntity>
    ) {
        apiGame.songs.forEach { apiSong ->
            processSong(
                apiSong,
                apiGame,
                composerEntities,
                songComposerJoins,
                tagKeyEntities,
                tagValueEntities,
                songTagValueJoins,
                songEntities
            )
        }
    }

    private fun processSong(
        apiSong: ApiSong,
        apiGame: VglsApiGame,
        composerEntities: MutableMap<Long, ComposerEntity>,
        songComposerJoins: ArrayList<SongComposerJoin>,
        tagKeyEntities: HashMap<String, TagKeyEntity>,
        tagValueEntities: HashMap<String, TagValueEntity>,
        songTagValueJoins: ArrayList<SongTagValueJoin>,
        songEntities: ArrayList<SongEntity>
    ) {
        val songEntity = apiSong.toEntity(
            apiGame.game_id + VglsApiGame.ID_OFFSET,
            apiGame.game_name
        )

        apiSong.composers.forEach { apiComposer ->
            if (songEntity.lyricPageCount > 0) {
                composerEntities[apiComposer.composer_id]?.hasVocalSongs = true
            }
            joinSongToComposer(apiSong, apiComposer, songComposerJoins)
        }

        apiSong.tags.forEach { tag ->
            processTag(
                tag,
                tagKeyEntities,
                tagValueEntities,
                apiSong,
                songTagValueJoins
            )
        }

        songEntities.add(songEntity)
    }

    private fun createComposerMap(apiComposers: List<ApiComposer>): MutableMap<Long, ComposerEntity> {
        val composers = mutableMapOf<Long, ComposerEntity>()
        apiComposers.forEach { apiComposer ->
            val entity = apiComposer.toEntity(false)
            composers[entity.id] = entity
        }
        return composers
    }

    private fun processTag(
        tagMapEntry: Map.Entry<String, List<String>>,
        tagKeyEntities: HashMap<String, TagKeyEntity>,
        tagValueEntities: HashMap<String, TagValueEntity>,
        apiSong: ApiSong,
        songTagValueJoins: ArrayList<SongTagValueJoin>
    ) {
        val key = tagMapEntry.key
            .toTitleCase()

        val values = tagMapEntry.value

        if (tagMapEntry.key != "song_id") {
            val existingKeyEntity = tagKeyEntities[key]

            val keyId = if (existingKeyEntity != null) {
                existingKeyEntity.id
            } else {
                val newKeyId = (tagKeyEntities.size + 1).toLong()
                val newEntity = TagKeyEntity(newKeyId, key)

                tagKeyEntities[key] = newEntity
                newKeyId
            }

            values.forEach { value ->
                joinSongToTagValue(key, value, tagValueEntities, keyId, apiSong, songTagValueJoins)
            }
        }
    }

    private fun joinSongToTagValue(
        key: String,
        value: String,
        tagValueEntities: HashMap<String, TagValueEntity>,
        keyId: Long,
        apiSong: ApiSong,
        songTagValueJoins: ArrayList<SongTagValueJoin>
    ) {
        val valueEntityMapKey = key + value
        val existingValueEntity = tagValueEntities[valueEntityMapKey]

        val valueToJoin = if (existingValueEntity == null) {
            val newValueId = (tagValueEntities.size + 1).toLong()
            val newEntity = TagValueEntity(
                newValueId,
                value.capitalize(),
                keyId,
                key
            )

            tagValueEntities[valueEntityMapKey] = newEntity
            newEntity
        } else {
            existingValueEntity
        }

        val join = SongTagValueJoin(apiSong.id, valueToJoin.id)
        songTagValueJoins.add(join)
    }

    private fun joinSongToComposer(
        apiSong: ApiSong,
        apiComposer: ApiComposer,
        songComposerJoins: ArrayList<SongComposerJoin>
    ) {
        val songComposerJoin = SongComposerJoin(
            apiSong.id,
            apiComposer.composer_id + ApiComposer.ID_OFFSET_COMPOSER
        )
        songComposerJoins.add(songComposerJoin)
    }

    @Suppress("DefaultLocale")
    private fun String.capitalize() = replaceFirstChar { char ->
        if (char.isLowerCase()) {
            char.titlecase(Locale.getDefault())
        } else {
            char.toString()
        }
    }

    private fun String.toTitleCase() = replace("_", " ")
        .split(" ")
        .joinToString(" ") {
            if (it != "the") {
                it.capitalize()
            } else {
                it
            }
        }

    private fun JamEntity.getCurrentSong(): Song? {
        val songId = currentSheetId

        return if (songId != null) {
            getSongSync(songId)?.toSong(null)
        } else {
            null
        }
    }

    companion object {
        const val CAPACITY = 500

        const val INTERVAL_JAM_REFRESH = 5000L

        val AGE_THRESHOLD = Duration.ofHours(4).toMillis()
    }
}
