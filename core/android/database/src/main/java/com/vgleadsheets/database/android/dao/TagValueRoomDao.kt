package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.TagValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagValueRoomDao : RoomDao<TagValueEntity> {
    @Query("SELECT * FROM tag_value WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun searchByName(name: String): Flow<List<TagValueEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<TagValueEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): TagValueEntity

    @Query("SELECT * FROM tag_value ORDER BY name COLLATE NOCASE")
    override fun getAll(): Flow<List<TagValueEntity>>

    @Insert
    override suspend fun insert(models: List<TagValueEntity>)

    @Query("DELETE FROM tag_value")
    override suspend fun nukeTable()

    companion object {
        const val QUERY_SINGLE = "SELECT * FROM tag_value WHERE id = :id"
    }
}
