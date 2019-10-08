package com.vgleadsheets.repository

import android.net.Uri
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.pages.PageEntity
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.search.SearchResult
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

    override fun getGames(): Observable<List<Game>> = gameDao.getAll()
        .map { gameEntities ->
            gameEntities.map { gameEntity ->
                val songs = songDao
                    .getSongsForGameSync(gameEntity.id)
                    .map { songEntity ->
                        val parts = partDao
                            .getPartsForSongId(songEntity.id)
                            .map { it.toPart(null) }

                        songEntity.toSong(null, parts)
                    }

                gameEntity.toGame(songs)
            }
        }

    override fun getSongsForGame(gameId: Long): Observable<List<Song>> = songDao
        .getSongsForGame(gameId)
        .map { songEntities ->
            songEntities.map { songEntity ->
                val composers = songComposerDao
                    .getComposersForSong(songEntity.id)
                    .map { composerEntity -> composerEntity.toComposer(null) }

                val parts = partDao
                    .getPartsForSongId(songEntity.id)
                    .map { it.toPart(null) }

                songEntity.toSong(composers, parts)
            }
        }

    override fun getSongsByComposer(composerId: Long): Observable<List<Song>> =
        songComposerDao
            .getSongsForComposer(composerId)
            .map { songEntities ->
                songEntities.map { songEntity ->
                    val composers = songComposerDao
                        .getComposersForSong(songEntity.id)
                        .map { composerEntity -> composerEntity.toComposer(null) }

                    val parts = partDao
                        .getPartsForSongId(songEntity.id)
                        .map { it.toPart(null) }

                    songEntity.toSong(composers, parts)
                }
            }

    override fun getSong(songId: Long): Observable<Song> = songDao
        .getSong(songId)
        .map {
            val parts = partDao
                .getPartsForSongId(songId)
                .map { partEntity ->
                    val pages = pageDao
                        .getPagesForPartId(partEntity.id)
                        .map { pageEntity -> pageEntity.toPage() }

                    partEntity.toPart(pages)
                }
            it.toSong(null, parts)
        }

    override fun getAllSongs(): Observable<List<Song>> = songDao
        .getAll()
        .map { songEntities ->
            songEntities.map { songEntity ->
                val composers = songComposerDao
                    .getComposersForSong(songEntity.id)
                    .map { composerEntity -> composerEntity.toComposer(null) }

                val parts = partDao
                    .getPartsForSongId(songEntity.id)
                    .map { partEntity ->
                        val pages = pageDao
                            .getPagesForPartId(partEntity.id)
                            .map { pageEntity -> pageEntity.toPage() }

                        partEntity.toPart(pages)
                    }
                songEntity.toSong(composers, parts)
            }
        }

    override fun getComposers(): Observable<List<Composer>> = composerDao
        .getAll()
        .map { composerEntities ->
            composerEntities.map { composerEntity ->
                val songs = songComposerDao
                    .getSongsForComposerSync(composerEntity.id)
                    .map { songEntity ->
                        val parts = partDao
                            .getPartsForSongId(songEntity.id)
                            .map { partEntity -> partEntity.toPart(null) }

                        songEntity.toSong(null, parts)
                    }

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
    override fun searchSongs(searchQuery: String): Observable<List<SearchResult>> {
        return songDao
            .searchSongsByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
            .map { songEntities -> songEntities.map { it.toSearchResult() } }
    }

    @Suppress("MaxLineLength")
    override fun searchGames(searchQuery: String): Observable<List<SearchResult>> {
        return gameDao
            .searchGamesByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
            .map { gameEntities -> gameEntities.map { it.toSearchResult() } }
    }

    @Suppress("MaxLineLength")
    override fun searchComposers(searchQuery: String): Observable<List<SearchResult>> {
        return composerDao
            .searchComposersByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
            .map { composerEntities -> composerEntities.map { it.toSearchResult() } }
    }

    override fun getLastUpdateTime(): Observable<Time> = getLastDbUpdateTime()

    override fun searchGiantBombForGame(vglsId: Long, name: String) {
        giantBombApi
            .searchForGame(name)
            .subscribe { response ->
                if (response.results.isNotEmpty()) {
                    val game = response.results[0]
                    Timber.d("Found Giant Bomb game ${game.name} with id ${game.id}")
                } else {
                    Timber.e("No game found from Giant Bomb with name $name.")
                }
            }
    }

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

                    val songEntity = apiSong.toSongEntity(apiGame.game_id)
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
