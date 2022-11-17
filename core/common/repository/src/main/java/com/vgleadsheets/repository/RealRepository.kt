package com.vgleadsheets.repository

import com.vgleadsheets.conversion.toModel
import com.vgleadsheets.coroutines.CustomFlows
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.TransactionRunner
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
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.SetlistEntry
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry
import com.vgleadsheets.model.relation.SongComposerRelation
import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiJam
import com.vgleadsheets.network.model.ApiSetlist
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.ApiSongHistoryEntry
import com.vgleadsheets.network.model.VglsApiGame
import com.vgleadsheets.tracking.Tracker
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.Instant

@Suppress("TooGenericExceptionCaught", "PrintStackTrace")
class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val transactionRunner: TransactionRunner,
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
) : VglsRepository {
    @Suppress("ReturnCount")
    override suspend fun checkShouldAutoUpdate(): Boolean {
        val lastCheckTime = withContext(dispatchers.disk) {
            getLastCheckTime()
        }

        if (threeTen.now().toInstant().toEpochMilli() - lastCheckTime.timeMs <= AGE_THRESHOLD) {
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

    override fun getAllGames(withSongs: Boolean) = gameDataSource.getAll(withSongs)
        .flowOn(dispatchers.disk)

    override fun getAllSongs(withComposers: Boolean) = songDataSource
        .getAll(withComposers)
        .flowOn(dispatchers.disk)

    override fun getAllComposers(withSongs: Boolean) = composerDataSource
        .getAll(withSongs)
        .flowOn(dispatchers.disk)

    override fun getAllTagKeys(withValues: Boolean) = tagKeyDataSource
        .getAll(withValues)
        .flowOn(dispatchers.disk)

    override fun getAllJams(withHistory: Boolean) = jamDataSource
        .getAll(withHistory)
        .flowOn(dispatchers.disk)

    override fun getSongsForGame(gameId: Long, withComposers: Boolean) = gameDataSource
        .getSongsForGame(gameId, withComposers)
        .flowOn(dispatchers.disk)

    override fun getSongsForTagValue(tagValueId: Long) = tagValueDataSource
        .getSongsForTagValue(tagValueId)
        .flowOn(dispatchers.disk)

    override fun getTagValuesForTagKey(tagKeyId: Long) = tagKeyDataSource
        .getTagValuesForTagKey(tagKeyId)
        .flowOn(dispatchers.disk)

    override fun getTagValuesForSong(songId: Long) = songDataSource
        .getTagValuesForSong(songId)
        .flowOn(dispatchers.disk)

    override fun getSetlistEntriesForJam(jamId: Long) = setlistEntryDataSource
        .getSetlistEntriesForJam(jamId)
        .flowOn(dispatchers.disk)

    override fun getSongHistoryForJam(jamId: Long) = songHistoryEntryDataSource
        .getSongHistoryEntriesForJam(jamId)
        .flowOn(dispatchers.disk)

    override fun getSong(
        songId: Long
    ) = songDataSource
        .getOneById(songId)
        .flowOn(dispatchers.disk)

    override fun getComposer(composerId: Long): Flow<Composer> = composerDataSource
        .getOneById(composerId)
        .flowOn(dispatchers.disk)

    override fun getGame(gameId: Long): Flow<Game> = gameDataSource
        .getOneById(gameId)
        .flowOn(dispatchers.disk)

    override fun getTagKey(tagKeyId: Long) = tagKeyDataSource
        .getOneById(tagKeyId)
        .flowOn(dispatchers.disk)

    override fun getTagValue(tagValueId: Long) = tagValueDataSource
        .getOneById(tagValueId)
        .flowOn(dispatchers.disk)

    override fun getJam(id: Long, withHistory: Boolean) = jamDataSource
        .getOneById(id)
        .flowOn(dispatchers.disk)

    override fun getLastUpdateTime(): Flow<Time> = getLastDbUpdateTime()

    @Suppress("MaxLineLength")
    override fun searchSongs(searchQuery: String) = songDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
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

    override suspend fun removeJam(id: Long) = withContext(dispatchers.disk) {
        jamDataSource.remove(id)
    }

    override suspend fun refreshJams() = withContext(dispatchers.network) {
        val jams = jamDataSource.getAll(false)
            .firstOrNull()

        jams?.forEach {
            refreshJamStateImpl(it.name)
        }

        Unit
    }

    override fun clearSheets() {
        gameDataSource.nukeTable()
        songDataSource.nukeTable()
        composerDataSource.nukeTable()
        tagKeyDataSource.nukeTable()
        tagValueDataSource.nukeTable()
        gameAliasDataSource.nukeTable()
        composerAliasDataSource.nukeTable()
        dbStatisticsDataSource.nukeTable()
    }

    override suspend fun clearJams() = withContext(dispatchers.disk) {
        songHistoryEntryDataSource.nukeTable()
        setlistEntryDataSource.nukeTable()
        jamDataSource.nukeTable()
    }

    @Suppress("MaxLineLength")
    private fun searchGames(searchQuery: String) = gameDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String) = composerDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchGameAliases(searchQuery: String) = gameAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull { it.game }
        }

    @Suppress("MaxLineLength")
    private fun searchComposerAliases(searchQuery: String) = composerAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull { it.composer }
        }

    private suspend fun refreshJamStateImpl(name: String) {
        try {
            // Perform network requests first
            val apiJam = withContext(dispatchers.network) {
                vglsApi.getJamState(name)
            }

            val setlist = withContext(dispatchers.network) {
                vglsApi.getSetlistForJam(name)
            }

            // Then pull the data out all at once
            val jam = Jam(
                apiJam.jam_id,
                name.lowercase(Locale.getDefault()),
                apiJam.song_history.firstOrNull()?.sheet_id,
                null,
            )

            val songHistoryEntries = songHistoryEntriesFromApiJam(apiJam.song_history, apiJam)
            val setlistEntries = setlistEntriesFromApiJam(setlist, apiJam)

            // Then stuff that data into storage
            withContext(dispatchers.disk) {
                transactionRunner.inTransaction {
                    try {
                        jamDataSource.insert(listOf(jam))
                        songHistoryEntryDataSource.insert(songHistoryEntries)
                        setlistEntryDataSource.insert(setlistEntries)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun setlistEntriesFromApiJam(
        setlist: ApiSetlist,
        apiJam: ApiJam
    ) = setlist.songs.mapIndexed { setlistIndex, entry ->
        SetlistEntry(
            MULTIPLIER_JAM_ID * apiJam.jam_id + setlistIndex,
            apiJam.jam_id,
            entry.game_name,
            entry.song_name,
            entry.id,
            null
        )
    }

    private fun songHistoryEntriesFromApiJam(
        songHistory: List<ApiSongHistoryEntry>,
        apiJam: ApiJam
    ) = if (songHistory.isNotEmpty()) {
        songHistory
            .drop(1)
            .mapIndexed { songHistoryIndex, songHistoryEntry ->
                SongHistoryEntry(
                    MULTIPLIER_JAM_ID * apiJam.jam_id + songHistoryIndex,
                    songHistoryEntry.sheet_id,
                    apiJam.jam_id,
                    null
                )
            }
    } else {
        emptyList()
    }

    private suspend fun getLastCheckTime() = dbStatisticsDataSource
        .getTime(TimeType.LAST_CHECKED.ordinal)
        .first()

    private fun getLastDbUpdateTime() = dbStatisticsDataSource
        .getTime(TimeType.LAST_UPDATED.ordinal)

    private suspend fun getLastDbUpdateTimeOnce() = getLastDbUpdateTime().first()

    private suspend fun getLastApiUpdateTime(): Time {
        try {
            val lastUpdate = vglsApi.getLastUpdateTime()

            val time = Time(
                TimeType.LAST_UPDATED.ordinal,
                Instant.parse(lastUpdate.last_updated).toEpochMilli()
            )

            dbStatisticsDataSource.insert(time)
            return time
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return Time(-1, 0L)
    }

    @Suppress("DefaultLocale", "LongMethod", "ComplexMethod")
    private suspend fun getDigest() {
        try {
            val digest = withContext(dispatchers.network) {
                vglsApi.getDigest()
            }

            val apiComposers = digest.composers
            val apiGames = digest.games

            val composers = createComposerMap(apiComposers)

            val games = apiGames.map { apiGame -> apiGame.toModel() }

            val songs = mutableListOf<Song>()
            val songComposerRelations = mutableListOf<SongComposerRelation>()
            val songTagValueRelations = mutableListOf<SongTagValueRelation>()

            val tagKeys = mutableMapOf<String, TagKey>()
            val tagValues = mutableMapOf<String, TagValue>()

            apiGames.forEach { apiGame ->
                processGame(
                    apiGame,
                    composers,
                    songComposerRelations,
                    tagKeys,
                    tagValues,
                    songTagValueRelations,
                    songs
                )
            }

            val dbStatistics = Time(
                TimeType.LAST_CHECKED.ordinal,
                threeTen.now().toInstant().toEpochMilli()
            )

            withContext(dispatchers.disk) {
                transactionRunner.inTransaction {
                    try {
                        clearSheets()

                        gameDataSource.insert(games)
                        composerDataSource.insert(composers.values.toList())
                        songDataSource.insert(songs)

                        tagKeyDataSource.insert(tagKeys.values.toList())
                        tagValueDataSource.insert(tagValues.values.toList())

                        composerDataSource.insertRelations(songComposerRelations)
                        tagValueDataSource.insertRelations(songTagValueRelations)

                        dbStatisticsDataSource.insert(dbStatistics)
                    } catch (ex: Exception) {
                        // Something in the storage write operation failed.
                        ex.printStackTrace()
                    }
                }
            }
        } catch (ex: Exception) {
            // Something in the non-storage part of this function failed.
            ex.printStackTrace()
        }
    }

    private fun processGame(
        apiGame: VglsApiGame,
        composers: MutableMap<Long, Composer>,
        songComposerRelations: MutableList<SongComposerRelation>,
        tagKeys: MutableMap<String, TagKey>,
        tagValues: MutableMap<String, TagValue>,
        songTagValueRelations: MutableList<SongTagValueRelation>,
        songs: MutableList<Song>
    ) {
        apiGame.songs.forEach { apiSong ->
            processSong(
                apiSong,
                apiGame,
                composers,
                songComposerRelations,
                tagKeys,
                tagValues,
                songTagValueRelations,
                songs
            )
        }
    }

    private fun processSong(
        apiSong: ApiSong,
        apiGame: VglsApiGame,
        composers: MutableMap<Long, Composer>,
        songComposerRelations: MutableList<SongComposerRelation>,
        tagKeys: MutableMap<String, TagKey>,
        tagValues: MutableMap<String, TagValue>,
        songTagValueRelations: MutableList<SongTagValueRelation>,
        songs: MutableList<Song>
    ) {
        val song = apiSong.toModel(
            apiGame.game_id,
            apiGame.game_name
        )

        apiSong.composers.forEach { apiComposer ->
            joinSongToComposer(apiSong, apiComposer, songComposerRelations)
        }

        apiSong.composers.forEach { apiComposer ->
            val composer = composers[apiComposer.composer_id] ?: return@forEach

            if (song.lyricPageCount > 0 && !composer.hasVocalSongs) {
                composers[apiComposer.composer_id] = composer.copy(hasVocalSongs = true)
            }
        }

        apiSong.tags.forEach { tag ->
            processTag(
                tag,
                tagKeys,
                tagValues,
                apiSong,
                songTagValueRelations
            )
        }

        songs.add(song)
    }

    private fun createComposerMap(apiComposers: List<ApiComposer>): MutableMap<Long, Composer> {
        val composers = mutableMapOf<Long, Composer>()
        apiComposers.forEach { apiComposer ->
            val entity = apiComposer.toModel()
            composers[entity.id] = entity
        }
        return composers
    }

    private fun processTag(
        tagMapEntry: Map.Entry<String, List<String>>,
        tagKeys: MutableMap<String, TagKey>,
        tagValues: MutableMap<String, TagValue>,
        apiSong: ApiSong,
        songTagValueRelations: MutableList<SongTagValueRelation>
    ) {
        val key = tagMapEntry.key
            .toTitleCase()

        val values = tagMapEntry.value

        if (tagMapEntry.key != "song_id") {
            val existingKey = tagKeys[key]

            val keyId = if (existingKey != null) {
                existingKey.id
            } else {
                val newKeyId = (tagKeys.size + 1).toLong()
                val new = TagKey(newKeyId, key, null)

                tagKeys[key] = new
                newKeyId
            }

            values.forEach { value ->
                joinSongToTagValue(key, value, tagValues, keyId, apiSong, songTagValueRelations)
            }
        }
    }

    private fun joinSongToTagValue(
        key: String,
        value: String,
        tagValues: MutableMap<String, TagValue>,
        keyId: Long,
        apiSong: ApiSong,
        songTagValueRelations: MutableList<SongTagValueRelation>
    ) {
        val valueMapKey = key + value
        val existingValue = tagValues[valueMapKey]

        val valueToRelation = if (existingValue == null) {
            val newValueId = (tagValues.size + 1).toLong()
            val new = TagValue(
                newValueId,
                value.capitalize(),
                keyId,
                key,
                null
            )

            tagValues[valueMapKey] = new
            new
        } else {
            existingValue
        }

        val join = SongTagValueRelation(apiSong.id, valueToRelation.id)
        songTagValueRelations.add(join)
    }

    private fun joinSongToComposer(
        apiSong: ApiSong,
        apiComposer: ApiComposer,
        songComposerRelations: MutableList<SongComposerRelation>
    ) {
        val songComposerRelation = SongComposerRelation(
            apiSong.id,
            apiComposer.composer_id
        )
        songComposerRelations.add(songComposerRelation)
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

    companion object {
        const val INTERVAL_JAM_REFRESH = 5000L

        const val MULTIPLIER_JAM_ID = 10_000L

        val AGE_THRESHOLD = Duration.ofHours(4).toMillis()
    }
}
