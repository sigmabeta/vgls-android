package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.ComposerPhoto
import com.vgleadsheets.model.relation.SongComposerRelation
import kotlinx.coroutines.flow.Flow

interface ComposerDataSource : DataSource<Composer> {
    fun getFavorites(): Flow<List<Composer>>

    fun getMostSongsComposers(): Flow<List<Composer>>

    fun getByIdList(ids: List<Long>): Flow<List<Composer>>

    fun searchByName(name: String): Flow<List<Composer>>

    suspend fun insertRelations(relations: List<SongComposerRelation>)

    fun getComposersForSong(songId: Long): Flow<List<Composer>>

    suspend fun getComposersForSongSync(songId: Long): List<Composer>

    suspend fun incrementSheetsPlayed(composerId: Long)

    suspend fun toggleFavorite(composerId: Long)

    suspend fun toggleOffline(composerId: Long)

    fun getHighestId(): Flow<Long>

    /** A page of composers in the same name order as [getAll], for the browse list's windowed loading. */
    suspend fun getPage(limit: Int, offset: Int): List<Composer>

    /**
     * Replaces every composer's vgm-metadata photo with [photoByComposerId]: composers in it get
     * that photo and credit, composers not in it lose theirs. Callers run it inside a transaction.
     */
    suspend fun replaceMetadataPhotos(photoByComposerId: Map<Long, ComposerPhoto>)

    /**
     * Whether any composer currently has a vgm-metadata photo. False after the table was cleared (or
     * before the first photo pull), which the updater treats as "the stored mapping is out of sync
     * with the database, fetch again" rather than trusting the ETag.
     */
    suspend fun hasMetadataPhoto(): Boolean
}
