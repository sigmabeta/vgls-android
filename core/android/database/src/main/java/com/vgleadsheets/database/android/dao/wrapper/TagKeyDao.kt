package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.tag.TagKey
import kotlinx.coroutines.flow.Flow

class TagKeyRoomDaoWrapper(
    private val roomImpl: RoomDao
): Dao {

    override fun getTagKey(tagKeyId: Long): Flow<TagKey>

    override fun getAll(): Flow<List<TagKey>>

    override suspend fun insertAll(tagKeyEntities: List<TagKey>)

    override suspend fun nukeTable()
}
