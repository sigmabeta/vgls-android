package com.vgleadsheets.repository

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.database.dao.TagValueDataSource
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.flowOn

@Suppress("TooGenericExceptionCaught", "PrintStackTrace")
class RealRepository(
    private val dispatchers: VglsDispatchers,
    private val gameDataSource: GameDataSource,
    private val composerDataSource: ComposerDataSource,
    private val tagKeyDataSource: TagKeyDataSource,
    private val tagValueDataSource: TagValueDataSource
) : VglsRepository {

    override fun getAllTagKeys() = tagKeyDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    override fun getTagValuesForTagKey(tagKeyId: Long) = tagValueDataSource
        .getTagValuesForTagKey(tagKeyId)
        .flowOn(dispatchers.disk)

    override fun getTagValuesForSong(songId: Long) = tagValueDataSource
        .getTagValuesForSong(songId)
        .flowOn(dispatchers.disk)

    override fun getTagKey(tagKeyId: Long) = tagKeyDataSource
        .getOneById(tagKeyId)
        .flowOn(dispatchers.disk)

    override fun getTagValue(tagValueId: Long) = tagValueDataSource
        .getOneById(tagValueId)
        .flowOn(dispatchers.disk)

    override suspend fun toggleFavoriteGame(gameId: Long) {
        gameDataSource.toggleFavorite(gameId)
    }

    override suspend fun toggleFavoriteComposer(composerId: Long) {
        composerDataSource.toggleFavorite(composerId)
    }

    override suspend fun toggleOfflineGame(gameId: Long) {
        gameDataSource.toggleOffline(gameId)
    }

    override suspend fun toggleOfflineComposer(composerId: Long) {
        composerDataSource.toggleOffline(composerId)
    }

    companion object {
        val AGE_THRESHOLD = 4.toDuration(DurationUnit.HOURS)
    }
}
