package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.alias.GameAliasEntity
import io.reactivex.Observable

@Dao
interface GameAliasDao {
    @Query("SELECT * FROM alias_game WHERE name LIKE :name ORDER BY name")
    fun getAliasesByName(name: String): Observable<List<GameAliasEntity>>

    @Insert
    fun insertAll(aliasEntities: List<GameAliasEntity>)

    @Query("DELETE FROM alias_game")
    fun nukeTable()
}
