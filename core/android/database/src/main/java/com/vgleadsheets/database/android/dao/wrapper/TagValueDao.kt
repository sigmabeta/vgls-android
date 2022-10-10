package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

class TagValueRoomDaoWrapper(
    private val roomImpl: RoomDao
): Dao {

    override fun getTagValue(tagValueId: Long): Flow<TagValue>

    override fun getValuesForTag(tagKeyId: Long): Flow<List<TagValue>>

    override fun getValuesForTagSync(tagKeyId: Long): List<TagValue>

    override suspend fun insertAll(tagValues: List<TagValue>)

    override suspend fun nukeTable()
}
