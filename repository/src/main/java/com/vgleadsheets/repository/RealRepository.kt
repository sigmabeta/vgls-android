package com.vgleadsheets.repository

import android.net.Uri
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.alias.ComposerAliasEntity
import com.vgleadsheets.model.alias.GameAliasEntity
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.giantbomb.GiantBombGame
import com.vgleadsheets.model.giantbomb.GiantBombPerson
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.pages.PageEntity
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.song.ApiSong
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.model.time.ThreeTenTime
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.time.TimeEntity
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.network.GiantBombApi
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import timber.log.Timber

@Suppress("TooManyFunctions")
class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val giantBombApi: GiantBombApi,
    private val baseImageUrl: String,
    private val threeTen: ThreeTenTime,
    database: VglsDatabase
) : Repository {

    private val gameDao = database.gameDao()
    private val songDao = database.songDao()
    private val composerDao = database.composerDao()
    private val partDao = database.partDao()
    private val pageDao = database.pageDao()
    private val songComposerDao = database.songComposerDao()
    private val dbStatisticsDao = database.dbStatisticsDao()
    private val gameAliasDao = database.gameAliasDao()
    private val composerAliasDao = database.composerAliasDao()

    override fun checkForUpdate(): Single<List<VglsApiGame>> {
        return getLastCheckTime()
            .filter { Instant.now().toEpochMilli() - it.time_ms > AGE_THRESHOLD }
            .flatMapSingle { getLastApiUpdateTime() }
            .zipWith<Time, Long>(getLastDbUpdateTimeOnce(), BiFunction { apiTime, dbTime ->
                apiTime.timeMs - dbTime.timeMs
            })
            .filter { diff ->
                diff > 0
            }
            .flatMapSingle { getDigest() }
    }

    override fun forceRefresh(): Single<List<VglsApiGame>> = getDigest()

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
                val parts = if (withParts) getPartsForSong(songEntity) else null
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
                    val parts = if (withParts) getPartsForSong(songEntity) else null
                    songEntity.toSong(composers, parts)
                }
            }

    override fun getSong(songId: Long): Observable<Song> = songDao
        .getSong(songId)
        .map {
            val parts = partDao
                .getPartsForSongId(songId)
                .map { partEntity ->
                    val pages = getPagesForPart(partEntity)
                    partEntity.toPart(pages)
                }
            it.toSong(null, parts)
        }

    override fun getAllSongs(withComposers: Boolean) = songDao
        .getAll()
        .map { songEntities ->
            songEntities.map { songEntity ->
                val composers = if (withComposers) getComposersForSong(songEntity) else null
                val parts = getPartsForSong(songEntity)
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

    override fun getComposer(composerId: Long): Observable<Composer> = composerDao
        .getComposer(composerId)
        .map { it.toComposer(null) }

    override fun getGame(gameId: Long): Observable<Game> = gameDao
        .getGame(gameId)
        .map { it.toGame(null) }

    @Suppress("MaxLineLength")
    override fun searchSongs(searchQuery: String) = songDao
        .searchSongsByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { songEntities ->
            songEntities.map {
                val parts = getPartsForSong(it)
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

    override fun searchGiantBombForGame(vglsId: Long, name: String) {
        giantBombApi
            .searchForGame(name)
            .flatMap { response ->
                val giantBombId: Long
                val photoUrl: String?
                if (response.results.isNotEmpty()) {
                    val game = response.results[0]

                    giantBombId = game.id
                    photoUrl = game.image.original_url

                    val aliasEntities = game.aliases
                        ?.split('\n')
                        ?.map { GameAliasEntity(vglsId, it, giantBombId, photoUrl) }

                    if (aliasEntities != null) {
                        gameAliasDao.insertAll(aliasEntities)
                    }
                } else {
                    giantBombId = GiantBombGame.ID_NOT_FOUND
                    photoUrl = null
                }

                return@flatMap gameDao.giantBombifyGame(vglsId, giantBombId, photoUrl)
            }
            .subscribe(
                {},
                {
                    Timber.e("Failed to retrieve game from GiantBomb: ${it.message}")
                }
            )
    }

    override fun searchGiantBombForComposer(vglsId: Long, name: String) {
        giantBombApi
            .searchForComposer(name)
            .flatMap { response ->
                val giantBombId: Long
                val photoUrl: String?
                if (response.results.isNotEmpty()) {
                    val composer = response.results[0]

                    giantBombId = composer.id
                    photoUrl = composer.image.original_url

                    val aliasEntities = composer.aliases
                        ?.split('\n')
                        ?.map { ComposerAliasEntity(vglsId, it, giantBombId, photoUrl) }

                    if (aliasEntities != null) {
                        composerAliasDao.insertAll(aliasEntities)
                    }
                } else {
                    giantBombId = GiantBombPerson.ID_NOT_FOUND
                    photoUrl = null
                }

                return@flatMap composerDao.giantBombifyComposer(vglsId, giantBombId, photoUrl)
            }
            .subscribe(
                {},
                {
                    Timber.e("Failed to retrieve person from GiantBomb: ${it.message}")
                }
            )
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

    private fun getSongsForGameEntity(gameEntity: GameEntity, withSongs: Boolean = true) =
        songDao.getSongsForGameSync(gameEntity.id)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSong(songEntity) else null
                songEntity.toSong(null, parts)
            }

    private fun getSongsForGameAlias(gameAliasEntity: GameAliasEntity, withSongs: Boolean = true) =
        songDao.getSongsForGameSync(gameAliasEntity.gameId)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSong(songEntity) else null
                songEntity.toSong(null, parts)
            }

    private fun getSongsForComposer(composerEntity: ComposerEntity, withSongs: Boolean = true) =
        songComposerDao.getSongsForComposerSync(composerEntity.id)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSong(songEntity) else null
                songEntity.toSong(null, parts)
            }

    private fun getSongsForComposerAlias(
        composerAliasEntity: ComposerAliasEntity,
        withSongs: Boolean = true
    ) =
        songComposerDao.getSongsForComposerSync(composerAliasEntity.composerId)
            .map { songEntity ->
                val parts = if (withSongs) getPartsForSong(songEntity) else null
                songEntity.toSong(null, parts)
            }

    private fun getPartsForSong(songEntity: SongEntity, withPages: Boolean = true) =
        partDao.getPartsForSongId(songEntity.id)
            .map { partEntity ->
                val pages = if (withPages) getPagesForPart(partEntity) else null
                partEntity.toPart(pages)
            }

    private fun getPagesForPart(partEntity: PartEntity) =
        pageDao.getPagesForPartId(partEntity.id)
            .map { pageEntity -> pageEntity.toPage() }

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

    private fun getLastCheckTime(): Single<TimeEntity> = dbStatisticsDao
        .getTime(TimeType.LAST_CHECKED.ordinal)
        .subscribeOn(Schedulers.io())
        .firstOrError()

    private fun getLastDbUpdateTime(): Observable<Time> = dbStatisticsDao
        .getTime(TimeType.LAST_UPDATED.ordinal)
        .subscribeOn(Schedulers.io())
        .map {
            it.toTime()
        }

    private fun getLastDbUpdateTimeOnce(): Single<Time> = getLastDbUpdateTime().firstOrError()

    private fun getLastApiUpdateTime() = vglsApi.getLastUpdateTime()
        .map { it.toTimeEntity(threeTen).toTime() }
        .doOnSuccess() {
            dbStatisticsDao.insert(
                TimeEntity(TimeType.LAST_UPDATED.ordinal, it.timeMs)
            )
        }

    @Suppress("LongMethod")
    private fun getDigest(): Single<List<VglsApiGame>> = vglsApi.getDigest()
        .doOnSuccess { apiGames ->
            val gameEntities = apiGames.map { apiGame -> apiGame.toGameEntity() }

            val songEntities = ArrayList<SongEntity>(CAPACITY)
            val partEntities = ArrayList<PartEntity>(CAPACITY)
            val pageEntities = ArrayList<PageEntity>(CAPACITY)
            val composerEntities = HashSet<ComposerEntity>(CAPACITY)
            val songComposerJoins = ArrayList<SongComposerJoin>(CAPACITY)

            var partCount = 0L
            apiGames.forEach { apiGame ->
                apiGame.songs.forEach { apiSong ->
                    apiSong.composers.forEach { apiComposer ->
                        val songComposerJoin =
                            SongComposerJoin(apiSong.id, apiComposer.id)
                        songComposerJoins.add(songComposerJoin)

                        val composerEntity = apiComposer.toComposerEntity()
                        composerEntities.add(composerEntity)
                    }

                    apiSong.files.parts.forEach {
                        partCount++
                        val partEntity =
                            it.value.toPartEntity(partCount, apiSong.id)
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

                    val songEntity = apiSong.toSongEntity(
                        apiGame.game_id,
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
                partDao,
                pageDao,
                songEntities,
                composerEntities.toList(),
                partEntities,
                pageEntities,
                songComposerJoins
            )

            dbStatisticsDao.insert(
                TimeEntity(TimeType.LAST_CHECKED.ordinal, Instant.now().toEpochMilli())
            )
        }

    companion object {
        const val CAPACITY = 500

        const val URL_SEPARATOR_FOLDER = "/"
        const val URL_SEPARATOR_NUMBER = "-"
        const val URL_FILE_EXT_PNG = ".png"

        val AGE_THRESHOLD = Duration.ofMinutes(60).toMillis()
    }
}
