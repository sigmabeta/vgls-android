package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.composer.ComposerEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ComposerDao {
    @Query("SELECT * FROM composer WHERE id = :composerId")
    fun getComposer(composerId: Long): Observable<ComposerEntity>

    @Query("SELECT * FROM composer ORDER BY name")
    fun getAll(): Observable<List<ComposerEntity>>

    @Query("SELECT * FROM composer WHERE name LIKE :name")
    fun searchComposersByName(name: String): Observable<List<ComposerEntity>>

    @Insert
    fun insertAll(composerEntities: List<ComposerEntity>)

    @Query("DELETE FROM composer")
    fun nukeTable()

    @Query("UPDATE composer SET giantBombId = :giantBombId, photoUrl = :photoUrl WHERE id = :vglsId;")
    fun giantBombifyComposer(vglsId: Long, giantBombId: Long, photoUrl: String?): Single<Int>
}
