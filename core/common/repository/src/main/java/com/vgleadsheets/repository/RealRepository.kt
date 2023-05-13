package com.vgleadsheets.repository

import com.vgleadsheets.conversion.toModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.TransactionRunner
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.ComposerAlias
import com.vgleadsheets.model.alias.GameAlias
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.relation.SongComposerRelation
import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.VglsApiGame
import com.vgleadsheets.tracking.Tracker
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
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
    private val hatchet: Hatchet,
    private val composerAliasDataSource: ComposerAliasDataSource,
    private val composerDataSource: ComposerDataSource,
    private val dbStatisticsDataSource: DbStatisticsDataSource,
    private val gameAliasDataSource: GameAliasDataSource,
    private val gameDataSource: GameDataSource,
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
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

    override fun refresh() = refreshInternal()

    override fun getAllGames(withSongs: Boolean) = gameDataSource
        .getAll(withSongs)
        .flowOn(dispatchers.disk)

    override fun getAllSongs(withComposers: Boolean) = songDataSource
        .getAll(withComposers)
        .flowOn(dispatchers.disk)

    override fun getAllComposers(withSongs: Boolean) = composerDataSource
        .getAll(withSongs)
        .flowOn(dispatchers.disk)

    override fun getFavoriteGames(withSongs: Boolean) =
        gameDataSource.getFavorites().flowOn(dispatchers.disk)

    override fun getFavoriteSongs(withComposers: Boolean) =
        songDataSource.getFavorites().flowOn(dispatchers.disk)

    override fun getFavoriteComposers(withSongs: Boolean) =
        composerDataSource.getFavorites().flowOn(dispatchers.disk)

    override fun getAllTagKeys(withValues: Boolean) = tagKeyDataSource
        .getAll(withValues)
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

    override fun getAliasesForSong(songId: Long) = songAliasDataSource
        .getAliasesForSong(songId)
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

    override fun getLastUpdateTime(): Flow<Time> = getLastDbUpdateTime()

    override fun searchGamesCombined(searchQuery: String) = combine(
        searchGames(searchQuery),
        searchGameAliases(searchQuery)
    ) { games: List<Game>, gameAliases: List<Game> ->
        games + gameAliases
    }.map { games ->
        games.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    override fun searchComposersCombined(searchQuery: String) = combine(
        searchComposers(searchQuery),
        searchComposerAliases(searchQuery)
    ) { composers: List<Composer>, composerAliases: List<Composer> ->
        composers + composerAliases
    }.map { composers ->
        composers.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    override fun searchSongsCombined(searchQuery: String) = combine(
        searchSongs(searchQuery),
        searchSongAliases(searchQuery)
    ) { songs: List<Song>, songAliases: List<Song> ->
        songs + songAliases
    }.map { songs ->
        songs.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    override suspend fun incrementViewCounter(songId: Long) {
        val song = songDataSource.getOneByIdSync(songId)

        songDataSource.incrementPlayCount(song.id)
        gameDataSource.incrementSheetsPlayed(song.gameId)

        song.composers?.forEach {
            composerDataSource.incrementSheetsPlayed(it.id)
        }
    }

    override suspend fun toggleFavoriteSong(songId: Long) {
        songDataSource.toggleFavorite(songId)
    }

    override suspend fun toggleFavoriteGame(gameId: Long) {
        gameDataSource.toggleFavorite(gameId)
    }

    override suspend fun toggleFavoriteComposer(composerId: Long) {
        composerDataSource.toggleFavorite(composerId)
    }

    override suspend fun toggleOfflineSong(songId: Long) {
        songDataSource.toggleOffline(songId)
    }

    override suspend fun toggleOfflineGame(gameId: Long) {
        gameDataSource.toggleOffline(gameId)
    }

    override suspend fun toggleOfflineComposer(composerId: Long) {
        composerDataSource.toggleOffline(composerId)
    }

    override suspend fun clearSheets() = withContext(dispatchers.disk) {
        gameDataSource.nukeTable()
        songDataSource.nukeTable()
        composerDataSource.nukeTable()
        tagKeyDataSource.nukeTable()
        tagValueDataSource.nukeTable()
        gameAliasDataSource.nukeTable()
        composerAliasDataSource.nukeTable()
        songAliasDataSource.nukeTable()
    }

    @Suppress("MaxLineLength")
    private fun searchGames(searchQuery: String) = gameDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String) = composerDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchSongs(searchQuery: String) = songDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .flowOn(dispatchers.disk)

    @Suppress("MaxLineLength")
    private fun searchGameAliases(searchQuery: String) = gameAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull {
                it.game?.copy(
                    name = it.name,
                    songs = gameDataSource
                        .getSongsForGame(it.game!!.id, false)
                        .first()
                )
            }
        }

    @Suppress("MaxLineLength")
    private fun searchComposerAliases(searchQuery: String) = composerAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull {
                it.composer?.copy(
                    name = it.name,
                    songs = composerDataSource
                        .getSongsForComposer(it.composer!!.id)
                        .first()
                )
            }
        }

    @Suppress("MaxLineLength")
    private fun searchSongAliases(searchQuery: String) = songAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull {
                it.song?.copy(
                    name = it.name,
                )
            }
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
    private fun refreshInternal() = combine(
        getDigest(),
        getAllGames(false).take(1),
        getAllComposers(false).take(1),
        getAllSongs(false).take(1)
    ) { digest, dbGames, dbComposers, dbSongs ->
        val apiComposers = digest.composers
        val apiGames = digest.games

        val dbGamesMap = dbGames.associateBy { it.id }
        val dbComposerMap = dbComposers.associateBy { it.id }
        val dbSongMap = dbSongs.associateBy { it.id }

        val games = apiGames.map { apiGame ->
            val dbGame = dbGamesMap[apiGame.game_id]
            apiGame.toModel(
                dbGame?.sheetsPlayed ?: 0,
                dbGame?.isFavorite ?: false,
                dbGame?.isAvailableOffline ?: false,
            )
        }

        val composerMap = apiComposers.associate {
            val dbComposer = dbComposerMap[it.composer_id]
            it.composer_id to it.toModel(
                dbComposer?.sheetsPlayed ?: 0,
                dbComposer?.isFavorite ?: false,
                dbComposer?.isAvailableOffline ?: false,
            )
        }.toMutableMap()

        val songs = mutableListOf<Song>()
        val songComposerRelations = mutableListOf<SongComposerRelation>()
        val songTagValueRelations = mutableListOf<SongTagValueRelation>()

        val tagKeys = mutableMapOf<String, TagKey>()
        val tagValues = mutableMapOf<String, TagValue>()

        val composerAliases = mutableListOf<ComposerAlias>()
        val songAliases = mutableListOf<SongAlias>()

        apiComposers.forEach { composer ->
            composerAliases.addAll(
                composer.aliases?.map {
                    ComposerAlias(
                        id = null,
                        composer.composer_id,
                        name = it,
                        composer = null
                    )
                } ?: emptyList()
            )
        }

        val gameAliases = mutableListOf<GameAlias>()

        apiGames.forEach { apiGame ->
            processGame(
                apiGame,
                composerMap,
                songComposerRelations,
                tagKeys,
                tagValues,
                songTagValueRelations,
                songs,
                dbSongMap,
                gameAliases,
                songAliases
            )
        }

        val lastChecked = Time(
            TimeType.LAST_CHECKED.ordinal,
            threeTen.now().toInstant().toEpochMilli()
        )

        val removedSongs = dbSongs.asIdSet { it.id } - songs.asIdSet { it.id }
        val removedGames = dbGames.asIdSet { it.id } - games.asIdSet { it.id }
        val removedComposers =
            dbComposerMap.values.asIdSet { it.id } - composerMap.values.asIdSet { it.id }

        withContext(dispatchers.disk) {
            transactionRunner.inTransaction {
                try {
                    songDataSource.remove(removedSongs.toList())
                    gameDataSource.remove(removedGames.toList())
                    composerDataSource.remove(removedComposers.toList())

                    gameDataSource.insert(games)
                    composerDataSource.insert(composerMap.values.toList())
                    songDataSource.insert(songs)

                    tagKeyDataSource.nukeTable()
                    tagKeyDataSource.insert(tagKeys.values.toList())

                    tagValueDataSource.nukeTable()
                    tagValueDataSource.insert(tagValues.values.toList())

                    gameAliasDataSource.nukeTable()
                    gameAliasDataSource.insert(gameAliases)

                    composerAliasDataSource.nukeTable()
                    composerAliasDataSource.insert(composerAliases)

                    songAliasDataSource.nukeTable()
                    songAliasDataSource.insert(songAliases)

                    composerDataSource.insertRelations(songComposerRelations)
                    tagValueDataSource.insertRelations(songTagValueRelations)

                    dbStatisticsDataSource.insert(lastChecked)
                } catch (ex: Exception) {
                    // Something in the storage write operation failed.
                    hatchet.e(this.javaClass.simpleName, "Error updating database: $ex.message")
                }
            }
        }
    }

    private fun getDigest() = flow {
        val digest = vglsApi.getDigest()
        emit(digest)
    }

    private fun processGame(
        apiGame: VglsApiGame,
        composers: MutableMap<Long, Composer>,
        songComposerRelations: MutableList<SongComposerRelation>,
        tagKeys: MutableMap<String, TagKey>,
        tagValues: MutableMap<String, TagValue>,
        songTagValueRelations: MutableList<SongTagValueRelation>,
        songs: MutableList<Song>,
        dbSongsMap: Map<Long, Song>,
        gameAliases: MutableList<GameAlias>,
        songAliases: MutableList<SongAlias>
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
                songs,
                dbSongsMap,
                songAliases
            )
        }

        gameAliases.addAll(
            apiGame.aliases?.map {
                GameAlias(
                    id = null,
                    apiGame.game_id,
                    name = it,
                    game = null
                )
            } ?: emptyList()
        )
    }

    private fun processSong(
        apiSong: ApiSong,
        apiGame: VglsApiGame,
        composers: MutableMap<Long, Composer>,
        songComposerRelations: MutableList<SongComposerRelation>,
        tagKeys: MutableMap<String, TagKey>,
        tagValues: MutableMap<String, TagValue>,
        songTagValueRelations: MutableList<SongTagValueRelation>,
        songs: MutableList<Song>,
        dbSongsMap: Map<Long, Song>,
        songAliases: MutableList<SongAlias>
    ) {
        val dbSong = dbSongsMap[apiSong.id]
        val song = apiSong.toModel(
            apiGame.game_id,
            apiGame.game_name,
            dbSong?.playCount ?: 0,
            dbSong?.isFavorite ?: false,
            dbSong?.isAvailableOffline ?: false,
            dbSong?.isAltSelected ?: false,
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

        songAliases.addAll(
            apiSong.aliases?.map {
                SongAlias(
                    id = null,
                    apiSong.id,
                    name = it,
                    song = null
                )
            } ?: emptyList()
        )

        songs.add(song)
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

    private fun <ModelType> Collection<ModelType>.asIdSet(idExtractor: (ModelType) -> Long) =
        map(idExtractor).toSet()

    companion object {
        const val INTERVAL_JAM_REFRESH = 5000L

        const val MULTIPLIER_JAM_ID = 10_000L

        val AGE_THRESHOLD = Duration.ofHours(4).toMillis()
    }
}
