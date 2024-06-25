package com.vgleadsheets.repository

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.conversion.asModel
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import java.util.Locale

class DbUpdater(
    private val vglsApi: VglsApi,
    private val transactionRunner: TransactionRunner,
    private val threeTen: ThreeTenTime,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
    private val composerAliasDataSource: ComposerAliasDataSource,
    private val composerDataSource: ComposerDataSource,
    private val gameAliasDataSource: GameAliasDataSource,
    private val gameDataSource: GameDataSource,
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
    private val tagKeyDataSource: TagKeyDataSource,
    private val tagValueDataSource: TagValueDataSource,
    private val dbStatisticsDataSource: DbStatisticsDataSource,
) {
    suspend fun clearSheets() {
        gameDataSource.nukeTable()
        songDataSource.nukeTable()
        composerDataSource.nukeTable()
        tagKeyDataSource.nukeTable()
        tagValueDataSource.nukeTable()
        gameAliasDataSource.nukeTable()
        composerAliasDataSource.nukeTable()
        songAliasDataSource.nukeTable()
    }

    @Suppress("DefaultLocale", "LongMethod", "ComplexMethod")
    fun refresh() = combine(
        getDigest(),
        getAllGames().take(1),
        getAllComposers().take(1),
        getAllSongs().take(1)
    ) { digestStatus, dbGames, dbComposers, dbSongs ->
        val digest = if (digestStatus is LCE.Content) {
            digestStatus.data
        } else {
            hatchet.w("New data not found, aborting update.")
            return@combine false
        }

        val apiComposers = digest.composers
        val apiGames = digest.games

        val dbGamesMap = dbGames.associateBy { it.id }
        val dbComposerMap = dbComposers.associateBy { it.id }
        val dbSongMap = dbSongs.associateBy { it.id }

        val games = apiGames.map { apiGame ->
            val hasVocalSongs = apiGame
                .songs
                .any { it.lyricsPageCount > 0 }

            val dbGame = dbGamesMap[apiGame.game_id]
            apiGame.asModel(
                dbGame?.sheetsPlayed ?: 0,
                dbGame?.isFavorite ?: false,
                dbGame?.isAvailableOffline ?: false,
                hasVocalSongs,
                apiGame.songs.size,
            )
        }

        val composerMap = apiComposers.associate { apiComposer ->
            val dbComposer = dbComposerMap[apiComposer.composer_id]
            apiComposer.composer_id to apiComposer.asModel(
                dbComposer?.sheetsPlayed ?: 0,
                dbComposer?.isFavorite ?: false,
                dbComposer?.isAvailableOffline ?: false,
                false,
                0,
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

        composerMap.replaceAll { key, composer ->
            val songCount = songComposerRelations
                .filter { it.composerId == composer.id }
                .size

            composer.copy(
                songCount = songCount
            )
        }

        val lastDbUpdate = Time(
            TimeType.LAST_DB_UPDATE.ordinal,
            threeTen.now().toInstant().toEpochMilli()
        )

        val removedSongs = dbSongs.asIdSet { it.id } - songs.asIdSet { it.id }
        val removedGames = dbGames.asIdSet { it.id } - games.asIdSet { it.id }
        val removedComposers =
            dbComposerMap.values.asIdSet { it.id } - composerMap.values.asIdSet { it.id }

        transactionRunner.inTransaction {
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

            dbStatisticsDataSource.insert(lastDbUpdate)
        }
        true
    }

    private fun getAllGames() = gameDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    private fun getAllSongs() = songDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    private fun getAllComposers() = composerDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    @Suppress("TooGenericExceptionCaught")
    private fun getDigest() = flow {
        try {
            val digest = vglsApi.getDigest()
            emit(LCE.Content(digest))
        } catch (ex: Exception) {
            hatchet.e("API request for digest failed: ${ex.message}")
            emit(LCE.Error(ex))
        }
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
        val song = apiSong.asModel(
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
}
