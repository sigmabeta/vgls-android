package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.TagKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagKeyRoomDao : RoomDao<TagKeyEntity> {
    @Query("SELECT * FROM tag_key WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun searchByName(name: String): Flow<List<TagKeyEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<TagKeyEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): TagKeyEntity

    @Query("SELECT * FROM tag_key ORDER BY name COLLATE NOCASE")
    override fun getAll(): Flow<List<TagKeyEntity>>

    @Insert
    override suspend fun insert(models: List<TagKeyEntity>)

    @Query("DELETE FROM tag_key")
    override suspend fun nukeTable()

    companion object {
        const val QUERY_SINGLE = "SELECT * FROM tag_key WHERE id = :id"
    }
}
