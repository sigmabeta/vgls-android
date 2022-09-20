package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.alias.GameAliasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameAliasDao {
    @Query("SELECT * FROM alias_game WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun getAliasesByName(name: String): Flow<List<GameAliasEntity>>

    @Insert
    suspend fun insertAll(aliasEntities: List<GameAliasEntity>)

    @Query("DELETE FROM alias_game")
    suspend fun nukeTable()
}
