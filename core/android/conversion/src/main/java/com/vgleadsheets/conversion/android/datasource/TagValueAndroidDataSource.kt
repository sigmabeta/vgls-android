package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.database.android.join.SongTagValueJoin
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue

class TagValueAndroidDataSource(
    private val convert: TagValueConverter,
    private val roomImpl: TagValueRoomDao,
) : AndroidDataSource<TagValueRoomDao, TagValue, TagValueEntity, TagValueConverter>(
    convert,
    roomImpl,
),
TagValueDataSource {
    override fun insertRelations(relations: List<SongTagValueRelation>) =
        roomImpl.insertJoins(
            relations.map {
                SongTagValueJoin(
                    it.songId,
                    it.tagValueId
                )
            }
        )

    override fun getTagValuesForTagKey(tagKeyId: Long) = roomImpl
        .getForTagKey(tagKeyId)
        .mapListTo { convert.entityToModel(it) }

    override fun getTagValuesForTagKeySync(tagKeyId: Long) = roomImpl
        .getForTagKeySync(tagKeyId)
        .map { convert.entityToModel(it) }

    override fun getTagValuesForSong(songId: Long) = roomImpl
        .getForSong(songId)
        .mapListTo { convert.entityToModel(it) }
}
