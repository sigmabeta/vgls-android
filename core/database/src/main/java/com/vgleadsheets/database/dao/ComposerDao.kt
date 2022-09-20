package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.composer.ComposerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposerDao {
    @Query("SELECT * FROM composer WHERE id = :composerId")
    fun getComposer(composerId: Long): Flow<ComposerEntity>

    @Query("SELECT * FROM composer ORDER BY name COLLATE NOCASE")
    fun getAll(): Flow<List<ComposerEntity>>

    @Query("SELECT * FROM composer WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun searchComposersByName(name: String): Flow<List<ComposerEntity>>

    @Insert
    suspend fun insertAll(composerEntities: List<ComposerEntity>)

    @Query("DELETE FROM composer")
    suspend fun nukeTable()
}
