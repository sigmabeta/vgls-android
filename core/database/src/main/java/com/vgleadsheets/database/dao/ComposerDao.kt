package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.composer.ComposerEntity
import io.reactivex.Observable

@Dao
interface ComposerDao {
    @Query("SELECT * FROM composer WHERE id = :composerId")
    fun getComposer(composerId: Long): Observable<ComposerEntity>

    @Query("SELECT * FROM composer ORDER BY name COLLATE NOCASE")
    fun getAll(): Observable<List<ComposerEntity>>

    @Query("SELECT * FROM composer WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun searchComposersByName(name: String): Observable<List<ComposerEntity>>

    @Insert
    fun insertAll(composerEntities: List<ComposerEntity>)

    @Query("DELETE FROM composer")
    fun nukeTable()
}
