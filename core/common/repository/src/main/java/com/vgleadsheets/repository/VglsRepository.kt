package com.vgleadsheets.repository

import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface VglsRepository {
    // Full Lists
    fun getAllTagKeys(): Flow<List<TagKey>>

    // Related Lists
    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>
    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>

    // Single items
    fun getTagKey(tagKeyId: Long): Flow<TagKey>
    fun getTagValue(tagValueId: Long): Flow<TagValue>

    // User data
    suspend fun toggleFavoriteGame(gameId: Long)
    suspend fun toggleFavoriteComposer(composerId: Long)
    suspend fun toggleOfflineGame(gameId: Long)
    suspend fun toggleOfflineComposer(composerId: Long)
}
