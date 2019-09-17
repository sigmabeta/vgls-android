package com.vgleadsheets.repository

import android.net.Uri
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.database.TableName
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.pages.PageEntity
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.song.ApiSong
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import io.reactivex.functions.Function3

class RealRepository constructor(
    private val vglsApi: VglsApi,
    private val baseImageUrl: String,
    database: VglsDatabase
) : Repository {

    private val gameDao = database.gameDao()
    private val songDao = database.songDao()
    private val composerDao = database.composerDao()
    private val partDao = database.partDao()
    private val pageDao = database.pageDao()
    private val songComposerDao = database.songComposerDao()
    private val dbStatisticsDao = database.dbStatisticsDao()

    // TODO Split this method
    @Suppress("LongMethod", "ComplexMethod")
    override fun getGames(force: Boolean): Observable<Data<List<Game>>> {
        return isTableFresh(TableName.GAME, force)
            .flatMap { fresh ->
                if (!fresh) {
                    vglsApi.getAllGames()
                        .doOnNext { apiGames ->
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
                                        val partEntity = it.value.toPartEntity(partCount, apiSong.id)
                                        partEntities.add(partEntity)

                                        val pageCount = if (partEntity.part == PartSelectorOption.VOCAL.apiId) {
                                            apiSong.lyricsPageCount
                                        } else {
                                            apiSong.pageCount
                                        }

                                        for (pageNumber in 1..pageCount) {
                                            val imageUrl =
                                                generateImageUrl(partEntity, apiSong, pageNumber)

                                            val pageEntity = PageEntity(null,
                                                pageNumber,
                                                partEntity.id,
                                                imageUrl)

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
                                dbStatisticsDao,
                                partDao,
                                pageDao,
                                songEntities,
                                composerEntities.toList(),
                                partEntities,
                                pageEntities,
                                songComposerJoins
                            )
                        }
                        .map<Data<List<Game>>?> { Network() }
                        .startWith(Empty())
                } else {
                    gameDao.getAll()
                        .filter { it.isNotEmpty() }
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
                        .map { Storage(it) }
                }
            }
    }

    override fun getSongs(gameId: Long): Observable<Data<List<Song>>> = songDao
        .getSongsForGame(gameId)
        .filter { it.isNotEmpty() }
        .map { songEntities ->
            songEntities.map { songEntity ->
                val composers = songComposerDao
                    .getComposersForSong(songEntity.id)
                    .map { composerEntity -> composerEntity.toComposer() }
                songEntity.toSong(composers)
            }
        }
        .map { Storage(it) }

    override fun getSong(songId: Long): Observable<Data<Song>> = songDao
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
            it.toSong(parts = parts)
        }
        .map { Storage(it) }

    override fun search(searchQuery: String): Observable<List<SearchResult>> =
        Observable.combineLatest(
            searchSongs(searchQuery),
            searchGames(searchQuery),
            searchComposers(searchQuery),
            Function3 { songs: List<SearchResult>, games: List<SearchResult>, composers: List<SearchResult> ->
                songs + games + composers
            }
        )

    @Suppress("MaxLineLength")
    private fun searchSongs(searchQuery: String): Observable<List<SearchResult>> {
        return songDao
            .searchSongsByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
            .map { songEntities -> songEntities.map { it.toSearchResult() } }
    }

    @Suppress("MaxLineLength")
    private fun searchGames(searchQuery: String): Observable<List<SearchResult>> {
        return gameDao
            .searchGamesByTitle("%$searchQuery%") // Percent characters allow characters before and after the query to match.
            .map { gameEntities -> gameEntities.map { it.toSearchResult() } }
    }

    @Suppress("MaxLineLength")
    private fun searchComposers(searchQuery: String): Observable<List<SearchResult>> {
        return composerDao
            .searchComposersByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
            .map { composerEntities -> composerEntities.map { it.toSearchResult() } }
    }

    private fun isTableFresh(tableName: TableName, force: Boolean): Observable<Boolean> {
        return dbStatisticsDao.getLastEditDate(tableName.ordinal)
            .map { lastEdit ->
                force || System.currentTimeMillis() - lastEdit.last_edit_time_ms < AGE_THRESHOLD
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

    companion object {
        const val AGE_THRESHOLD = 60000L
        const val CAPACITY = 500

        const val URL_SEPARATOR_FOLDER = "/"
        const val URL_SEPARATOR_NUMBER = "-"
        const val URL_FILE_EXT_PNG = ".png"
    }
}
