package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.composer.ComposerEntity
import io.reactivex.Observable

@Dao
interface ComposerDao {
    @Query("SELECT * FROM composer WHERE name LIKE :name")
    fun searchComposersByName(name: String): Observable<List<ComposerEntity>>

    @Insert
    fun insertAll(composerEntities: List<ComposerEntity>): LongArray

    @Query("DELETE FROM composer")
    fun nukeTable()
}
