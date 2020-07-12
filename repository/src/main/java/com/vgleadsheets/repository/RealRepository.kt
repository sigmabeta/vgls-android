package com.vgleadsheets.repository

import android.annotation.SuppressLint
import android.net.Uri
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.alias.ComposerAliasEntity
import com.vgleadsheets.model.alias.GameAliasEntity
import com.vgleadsheets.model.composer.ApiComposer
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.joins.SongTagValueJoin
import com.vgleadsheets.model.pages.PageEntity
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.song.ApiSong
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.model.tag.TagKeyEntity
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.tag.TagValueEntity
import com.vgleadsheets.model.time.ThreeTenTime
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.time.TimeEntity
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.VglsApi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Duration
import java.util.Locale
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val baseImageUrl: String,
    private val threeTen: ThreeTenTime,
    database: VglsDatabase
) : Repository {

    private val gameDao = database.gameDao()
    private val songDao = database.songDao()
    private val composerDao = database.composerDao()
    private val partDao = database.partDao()
    private val pageDao = database.pageDao()
    private val tagKeyDao = database.tagKeyDao()
    private val tagValueDao = database.tagValueDao()
    private val songComposerDao = database.songComposerDao()
    private val songTagValueDao = database.songTagValueDao()
    private val dbStatisticsDao = database.dbStatisticsDao()
    private val gameAliasDao = database.gameAliasDao()
    private val composerAliasDao = database.composerAliasDao()
    private val jamDao = database.jamDao()
    private val setlistEntryDao = database.setlistEntryDao()
    private val songHistoryEntryDao = database.songHistoryEntryDao()

    @ExperimentalStdlibApi
    override fun checkForUpdate(): Single<ApiDigest> {
        return getLastCheckTime()
            .filter { threeTen.now().toInstant().toEpochMilli() - it.time_ms > AGE_THRESHOLD }
            .flatMapSingle { getLastApiUpdateTime() }
            .zipWith<Time, Long>(getLastDbUpdateTimeOnce(), BiFunction { apiTime, dbTime ->
                apiTime.timeMs - dbTime.timeMs
            })
            .filter { diff ->
                diff > 0
            }
            .flatMapSingle { getDigest() }
    }

    @ExperimentalStdlibApi
    override fun forceRefresh(): Single<ApiDigest> = getDigest()

    override fun observeJamState(id: Long) = jamDao
        .getJam(id)
        .map {
            val currentSong = getSongSync(it.currentSheetId)?.toSong(null, null)
            it.toJam(currentSong, null)
        }

    override fun refreshJamStateContinuously(name: String) = Observable.interval(
        INTERVAL_JAM_REFRESH,
        TimeUnit.MILLISECONDS
    ).flatMap { refreshJamStateImpl(name) }

    override fun refreshJamState(name: String) = refreshJamStateImpl(name)
        .firstOrError()

    override fun refreshSetlist(jamId: Long, name: String) = refreshSetlistImpl(jamId, name)

    override fun getGames(withSongs: Boolean): Observable<List<Game>> = gameDao.getAll()
        .map { gameEntities ->
            gameEntities.map { gameEntity ->
                val songs = if (withSongs) getSongsForGameEntity(gameEntity) else null
                gameEntity.toGame(songs)
            }
        }

    override fun getSongsForGame(
        gameId: Long,
        withParts: Boolean,
        withComposers: Boolean
    ) = songDao
        .getSongsForGame(gameId)
        .map { songEntities ->
            songEntities.map { songEntity ->
                val parts = if (withParts) getPartsForSongSync(songEntity.id) else null
                val composers = if (withComposers) getComposersForSong(songEntity) else null
                songEntity.toSong(composers, parts)
            }
        }

    override fun getSongsByComposer(
        composerId: Long,
        withParts: Boolean
    ): Observable<List<Song>> =
        songComposerDao
            .getSongsForComposer(composerId)
            .map { songEntities ->
                songEntities.map { songEntity ->
                    val composers = null
                    val parts = if (withParts) getPartsForSongSync(songEntity.id) else null
                    songEntity.toSong(composers, parts)
                }
            }

    override fun getTagValuesForTagKey(tagKeyId: Long, withSongs: Boolean) =
        tagValueDao.getValuesForTag(tagKeyId)
            .map { tagValueEntities ->
                tagValueEntities.map { tagValueEntity ->
                    val songs = if (withSongs) getSongsForTagValueSync(tagValueEntity) else null
                    tagValueEntity.toTagValue(songs)
                }
            }

    override fun getSongsForTagValue(
        tagValueId: Long,
        withParts: Boolean
    ): Observable<List<Song>> =
        songTagValueDao
            .getSongsForTagValue(tagValueId)
            .map { songEntities ->
                songEntities.map { songEntity ->
                    val composers = null
                    val parts = if (withParts) getPartsForSongSync(songEntity.id) else null
                    songEntity.toSong(composers, parts)
                }
            }

    override fun getTagValuesForSong(
        songId: Long
    ): Observable<List<TagValue>> =
        songTagValueDao
            .getTagValuesForSong(songId)
            .map { tagValueEntities ->
                tagValueEntities.map { tagValueEntity ->
                    tagValueEntity.toTagValue(null)
                }
            }

    override fun getSetlistForJam(jamId: Long) = setlistEntryDao
        .getSetlistEntriesForJam(jamId)
        .map { entryEntities ->
            entryEntities.map { entryEntity ->
                val parts = getPartsForSongSync(entryEntity.song_id)
                val song = getSongSync(entryEntity.song_id)?.toSong(null, parts)
                entryEntity.toSetlistEntry(song)
            }
        }

    override fun getPartsForSong(songId: Long, withPages: Boolean) =
        getPartsForSongImpl(songId, withPages)

    override fun getSong(
        songId: Long,
        withParts: Boolean,
        withComposers: Boolean
    ): Observable<Song> = songDao
        .getSong(songId)
        .map {
            val composers = if (withComposers) getComposersForSong(it) else null
            val parts = if (withParts) getPartsForSongSync(it.id) else null
            it.toSong(composers, parts)
        }

    override fun getAllSongs(withComposers: Boolean, withParts: Boolean) = songDao
        .getAll()
        .map { songEntities ->
            songEntities.map { songEntity ->
                val composers = if (withComposers) getComposersForSong(songEntity) else null
                val parts = if (withParts) getPartsForSongSync(songEntity.id) else null
                songEntity.toSong(composers, parts)
            }
        }

    override fun getComposers(withSongs: Boolean): Observable<List<Composer>> = composerDao
        .getAll()
        .map { composerEntities ->
            composerEntities.map { composerEntity ->
                val songs = getSongsForComposer(composerEntity)
                composerEntity.toComposer(songs)
            }
        }

    override fun getAllTagKeys(withValues: Boolean) = tagKeyDao
        .getAll()
        .map { tagKeyEntities ->
            tagKeyEntities.map {
                val values = if (withValues) getTagValuesForTagKeySync(it, false) else null
                it.toTagKey(values)
            }
        }

    override fun getJam(id: Long, withHistory: Boolean) = jamDao
        .getJam(id)
        .map {
            val parts = getPartsForSongSync(it.currentSheetId)
            val currentSong = getSongSync(it.currentSheetId)?.toSong(null, parts)

            val songHistory = if (withHistory) getSongHistoryForJamSync(id) else null

            it.toJam(currentSong, songHistory)
        }

    override fun getJams() = jamDao
        .getAll()
        .map {
            it.map {
                val currentSong = getSongSync(it.currentSheetId)?.toSong(null, null)
                it.toJam(currentSong, null)
            }
        }

    override fun getComposer(composerId: Long): Observable<Composer> = composerDao
        .getComposer(composerId)
        .map { it.toComposer(null) }

    override fun getGame(gameId: Long): Observable<Game> = gameDao
        .getGame(gameId)
        .map { it.toGame(null) }

    override fun getTagKey(tagKeyId: Long) = tagKeyDao
        .getTagKey(tagKeyId)
        .map { it.toTagKey(null) }

    override fun getTagValue(tagValueId: Long) = tagValueDao
        .getTagValue(tagValueId)
        .map { it.toTagValue(null) }

    @Suppress("MaxLineLength")
    override fun searchSongs(searchQuery: String) = songDao
        .searchSongsByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { songEntities ->
            songEntities.map {
                val parts = getPartsForSongSync(it.id)
                val composers = getComposersForSong(it)
                it.toSong(composers, parts)
            }
        }

    override fun searchGamesCombined(searchQuery: String) = Observable.combineLatest(
        searchGames(searchQuery),
        searchGameAliases(searchQuery),
        BiFunction { games: List<Game>, gameAliases: List<Game> ->
            games + gameAliases
        }
    ).map { games ->
        games.distinctBy { it.id }
    }

    override fun searchComposersCombined(searchQuery: String) = Observable.combineLatest(
        searchComposers(searchQuery),
        searchComposerAliases(searchQuery),
        BiFunction { composers: List<Composer>, composerAliases: List<Composer> ->
            composers + composerAliases
        }
    ).map { composers ->
        composers.distinctBy { it.id }
    }

    override fun getLastUpdateTime(): Observable<Time> = getLastDbUpdateTime()

    override fun removeJam(id: Long) = Completable.fromAction {
        songHistoryEntryDao.removeAllForJam(id)
        setlistEntryDao.removeAllForJam(id)
        jamDao.remove(id)
    }

    override fun clearSheets() = Completable.fromAction {
        gameDao.nukeTable()
        songDao.nukeTable()
        composerDao.nukeTable()
        songComposerDao.nukeTable()
        partDao.nukeTable()
        pageDao.nukeTable()
        tagKeyDao.nukeTable()
        tagValueDao.nukeTable()
        songTagValueDao.nukeTable()
        gameAliasDao.nukeTable()
        composerAliasDao.nukeTable()
        dbStatisticsDao.nukeTable()
    }.subscribeOn(Schedulers.io())

    override fun clearJams() = Completable.fromAction {
        songHistoryEntryDao.nukeTable()
        setlistEntryDao.nukeTable()
        jamDao.nukeTable()
    }.subscribeOn(Schedulers.io())

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

    private fun getSongsForGameEntity(gameEntity: GameEntity, withSongs: Boolean = true) =
        songDao.getSongsForGameSync(gameEntity.id)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSongSync(songEntity.id) else null
                songEntity.toSong(null, parts)
            }

    private fun getSongsForGameAlias(gameAliasEntity: GameAliasEntity, withSongs: Boolean = true) =
        songDao.getSongsForGameSync(gameAliasEntity.gameId)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSongSync(songEntity.id) else null
                songEntity.toSong(null, parts)
            }

    private fun getSongsForComposer(composerEntity: ComposerEntity, withSongs: Boolean = true) =
        songComposerDao.getSongsForComposerSync(composerEntity.id)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSongSync(songEntity.id) else null
                songEntity.toSong(null, parts)
            }

    private fun getTagValuesForTagKeySync(tagKeyEntity: TagKeyEntity, withSongs: Boolean = true) =
        tagValueDao.getValuesForTagSync(tagKeyEntity.id)
            .map { tagValueEntity ->
                val songs = if (withSongs) getSongsForTagValueSync(tagValueEntity) else null
                tagValueEntity.toTagValue(songs)
            }

    private fun getSongsForTagValueSync(tagValueEntity: TagValueEntity, withParts: Boolean = true) =
        songTagValueDao
            .getSongsForTagValueSync(tagValueEntity.id)
            .map { songEntity ->
                val parts = if (withParts) getPartsForSongSync(songEntity.id) else null
                songEntity.toSong(null, parts)
            }

    private fun getSongsForComposerAlias(
        composerAliasEntity: ComposerAliasEntity,
        withSongs: Boolean = true
    ) = songComposerDao.getSongsForComposerSync(composerAliasEntity.composerId)
        .map { songEntity ->
            val parts = if (withSongs) getPartsForSongSync(songEntity.id) else null
            songEntity.toSong(null, parts)
        }

    // TODO Merge this and the below method.
    private fun getPartsForSongSync(songId: Long, withPages: Boolean = true) = partDao
        .getPartsForSongIdSync(songId)
        .map { partEntity ->
            val pages = if (withPages) getPagesForPart(partEntity) else null
            partEntity.toPart(pages)
        }

    private fun getPartsForSongImpl(songId: Long, withPages: Boolean = true) = partDao
        .getPartsForSongId(songId)
        .map { partEntities ->
            partEntities.map { partEntity ->
                val pages = if (withPages) getPagesForPart(partEntity) else null
                partEntity.toPart(pages)
            }
        }

    private fun getPagesForPart(partEntity: PartEntity) =
        pageDao.getPagesForPartId(partEntity.id)
            .map { pageEntity -> pageEntity.toPage() }

    private fun getSongHistoryForJamSync(jamId: Long) = songHistoryEntryDao
        .getSongHistoryEntriesForJamSync(jamId)
        .map { songHistoryEntryEntity ->
            val parts = getPartsForSongSync(songHistoryEntryEntity.song_id)
            val song = getSongSync(songHistoryEntryEntity.song_id)?.toSong(null, parts)

            songHistoryEntryEntity.toSongHistoryEntry(song)
        }

    private fun getSongSync(songId: Long) = songDao.getSongSync(songId)

    private fun refreshJamStateImpl(name: String) = vglsApi.getJamState(name)
        .doOnNext {
            var songHistoryIndex = 0
            val songHistoryEntries = it.song_history
                .drop(1)
                .map { songHistoryEntry ->
                    songHistoryEntry.toSongHistoryEntryEntity(it.jam_id, songHistoryIndex++)
                }

            val jamEntity = it.toJamEntity(name.toLowerCase(Locale.getDefault()))

            jamDao.upsertJam(songHistoryEntryDao, jamEntity, songHistoryEntries)
            return@doOnNext
        }

    private fun refreshSetlistImpl(jamId: Long, name: String) = vglsApi.getSetlistForJam(name)
        .map { it.songs }
        .map { setlist ->
            var setlistIndex = 0
            setlist.map { entry ->
                entry.toSetlistEntryEntity(jamId, setlistIndex++)
            }
        }
        .doOnSuccess { setlistEntryDao.removeAllForJam(jamId) }
        .flatMap { setlistEntryDao.insertAll(it) }

    private fun generateImageUrl(
        partEntity: PartEntity,
        apiSong: ApiSong,
        pageNumber: Int
    ): String {
        return baseImageUrl +
                partEntity.part + URL_SEPARATOR_FOLDER +
                Uri.encode(apiSong.filename) + URL_SEPARATOR_NUMBER +
                pageNumber + URL_FILE_EXT_PNG
    }

    private fun getLastCheckTime() = dbStatisticsDao
        .getTime(TimeType.LAST_CHECKED.ordinal)
        .map { it.firstOrNull() ?: TimeEntity(TimeType.LAST_CHECKED.ordinal, 0L) }
        .firstOrError()
        .subscribeOn(Schedulers.io())

    private fun getLastDbUpdateTime() = dbStatisticsDao
        .getTime(TimeType.LAST_UPDATED.ordinal)
        .subscribeOn(Schedulers.io())
        .map { it.firstOrNull() ?: TimeEntity(TimeType.LAST_UPDATED.ordinal, 0L) }
        .map {
            it.toTime()
        }

    private fun getLastDbUpdateTimeOnce(): Single<Time> = getLastDbUpdateTime().firstOrError()

    private fun getLastApiUpdateTime() = vglsApi.getLastUpdateTime()
        .map { it.toTimeEntity().toTime() }
        .doOnSuccess() {
            dbStatisticsDao.insert(
                TimeEntity(TimeType.LAST_UPDATED.ordinal, it.timeMs)
            )
        }

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("DefaultLocale")
    @Suppress("LongMethod", "ComplexMethod")
    private fun getDigest(): Single<ApiDigest> = vglsApi
        .getDigest()
        .doOnSuccess { digest ->
            val apiComposers = digest.composers
            val apiGames = digest.games

            val composerEntities = apiComposers.map { apiComposer -> apiComposer.toComposerEntity() }
            val gameEntities = apiGames.map { apiGame -> apiGame.toGameEntity() }

            val songEntities = ArrayList<SongEntity>(CAPACITY)
            val partEntities = ArrayList<PartEntity>(CAPACITY)
            val pageEntities = ArrayList<PageEntity>(CAPACITY)
            val songComposerJoins = ArrayList<SongComposerJoin>(CAPACITY)
            val songTagValueJoins = ArrayList<SongTagValueJoin>(CAPACITY)

            val tagKeyEntities = HashMap<String, TagKeyEntity>(CAPACITY)
            val tagValueEntities = HashMap<String, TagValueEntity>(CAPACITY)

            var partCount = 0L
            apiGames.forEach { apiGame ->
                apiGame.songs.forEach { apiSong ->
                    apiSong.composers.forEach { apiComposer ->
                        val songComposerJoin = SongComposerJoin(
                            apiSong.id,
                            apiComposer.composer_id + ApiComposer.ID_OFFSET
                        )
                        songComposerJoins.add(songComposerJoin)
                    }

                    apiSong.parts.forEach { partId ->
                        partCount++
                        val partEntity = PartEntity(partCount, apiSong.id, partId)
                        partEntities.add(partEntity)

                        val pageCount =
                            if (partEntity.part == PartSelectorOption.VOCAL.apiId) {
                                apiSong.lyricsPageCount
                            } else {
                                apiSong.pageCount
                            }

                        for (pageNumber in 1..pageCount) {
                            val imageUrl =
                                generateImageUrl(partEntity, apiSong, pageNumber)

                            val pageEntity = PageEntity(
                                null,
                                pageNumber,
                                partEntity.id,
                                imageUrl
                            )

                            pageEntities.add(pageEntity)
                        }
                    }

                    apiSong.tags.forEach { tagMapEntry ->
                        val key = tagMapEntry.key
                            .toTitleCase()

                        val values = tagMapEntry.value

                        if (tagMapEntry.key != "song_id") {
                            val existingKeyEntity = tagKeyEntities.get(key)

                            val keyId = if (existingKeyEntity != null) {
                                existingKeyEntity.id
                            } else {
                                val newKeyId = (tagKeyEntities.size + 1).toLong()
                                val newEntity = TagKeyEntity(newKeyId, key)

                                tagKeyEntities.put(key, newEntity)
                                newKeyId
                            }

                            values.forEach { value ->
                                val valueEntityMapKey = key + value
                                val existingValueEntity = tagValueEntities
                                    .get(valueEntityMapKey)

                                val valueToJoin = if (existingValueEntity == null) {
                                    val newValueId = (tagValueEntities.size + 1).toLong()
                                    val newEntity = TagValueEntity(
                                        newValueId,
                                        value.capitalize(Locale.getDefault()),
                                        keyId,
                                        key
                                    )

                                    tagValueEntities.put(valueEntityMapKey, newEntity)
                                    newEntity
                                } else {
                                    existingValueEntity
                                }

                                val join = SongTagValueJoin(apiSong.id, valueToJoin.id)
                                songTagValueJoins.add(join)
                            }
                        }
                    }

                    val songEntity = apiSong.toSongEntity(
                        apiGame.game_id + VglsApiGame.ID_OFFSET,
                        apiGame.game_name
                    )

                    songEntities.add(songEntity)
                }
            }

            gameDao.refreshTable(
                gameEntities,
                songDao,
                composerDao,
                songComposerDao,
                songTagValueDao,
                partDao,
                pageDao,
                tagKeyDao,
                tagValueDao,
                songEntities,
                composerEntities.toList(),
                partEntities,
                pageEntities,
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

    companion object {
        const val CAPACITY = 500

        const val INTERVAL_JAM_REFRESH = 5000L

        const val URL_SEPARATOR_FOLDER = "/"
        const val URL_SEPARATOR_NUMBER = "-"
        const val URL_FILE_EXT_PNG = ".png"

        val AGE_THRESHOLD = Duration.ofHours(4).toMillis()
    }
}

@OptIn(ExperimentalStdlibApi::class)
@SuppressLint("DefaultLocale")
private fun String.toTitleCase() = this
    .replace("_", " ")
    .split(" ")
    .map {
        if (it != "the") {
            it.capitalize(Locale.getDefault())
        } else {
            it
        }
    }
    .joinToString(" ")
