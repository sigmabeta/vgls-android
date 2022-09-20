package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.alias.ComposerAliasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComposerAliasDao {
    @Query("SELECT * FROM alias_composer WHERE name LIKE :name")
    fun getAliasesByName(name: String): Flow<List<ComposerAliasEntity>>

    @Insert
    suspend fun insertAll(aliasEntities: List<ComposerAliasEntity>)

    @Query("DELETE FROM alias_composer")
    suspend fun nukeTable()
}
