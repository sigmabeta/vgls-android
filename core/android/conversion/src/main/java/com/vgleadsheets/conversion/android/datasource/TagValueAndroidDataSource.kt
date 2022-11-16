package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.SongsForTagValueDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.database.android.join.SongTagValueJoin
import com.vgleadsheets.database.dao.TagValueDataSource
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.relation.SongTagValueRelation
import com.vgleadsheets.model.tag.TagValue

class TagValueAndroidDataSource(
    private val convert: TagValueConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: TagValueRoomDao,
    private val relatedRoomImpl: SongsForTagValueDao
) : OneToManyAndroidDataSource<TagValueRoomDao, TagValue, TagValueEntity, Song, SongEntity, SongsForTagValueDao, TagValueConverter, SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
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

    override fun getSongsForTagValue(tagValueId: Long) = relatedRoomImpl
        .getJoinedEntities(tagValueId)
        .mapList {
            manyConverter.entityToModel(it)
        }
}
