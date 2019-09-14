package com.vgleadsheets.repository

import com.vgleadsheets.database.TableName
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import io.reactivex.functions.Function3

class RealRepository constructor(
    private val vglsApi: VglsApi,
    database: VglsDatabase
) : Repository {

    private val gameDao = database.gameDao()
    private val songDao = database.songDao()
    private val composerDao = database.composerDao()
    private val partDao = database.partDao()
    private val songComposerDao = database.songComposerDao()
    private val dbStatisticsDao = database.dbStatisticsDao()

    override fun getGames(force: Boolean): Observable<Data<List<Game>>> {
        return isTableFresh(TableName.GAME, force)
            .flatMap { fresh ->
                if (!fresh) {
                    vglsApi.getAllGames()
                        .doOnNext { apiGames ->
                            val gameEntities = apiGames.map { apiGame -> apiGame.toGameEntity() }

                            val songEntities = ArrayList<SongEntity>(CAPACITY)
                            val partEntities = ArrayList<PartEntity>(CAPACITY)
                            val composerEntities = HashSet<ComposerEntity>(CAPACITY)
                            val songComposerJoins = ArrayList<SongComposerJoin>(CAPACITY)

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
                                        val partEntity = it.value.toPartEntity(apiSong.id)
                                        partEntities.add(partEntity)
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
                                songEntities,
                                composerEntities.toList(),
                                partEntities,
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
                                        songEntity.toSong(null, null)
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
                .map { partEntity -> partEntity.toPart() }
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

    companion object {
        const val AGE_THRESHOLD = 60000L
        const val CAPACITY = 500
    }
}
