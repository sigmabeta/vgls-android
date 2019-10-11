package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.alias.ComposerAliasEntity
import io.reactivex.Observable

@Dao
interface ComposerAliasDao {
    @Query("SELECT * FROM alias_composer WHERE name LIKE :name")
    fun getAliasesByName(name: String): Observable<List<ComposerAliasEntity>>

    @Insert
    fun insertAll(aliasEntities: List<ComposerAliasEntity>)

    @Query("DELETE FROM alias_composer")
    fun nukeTable()
}
