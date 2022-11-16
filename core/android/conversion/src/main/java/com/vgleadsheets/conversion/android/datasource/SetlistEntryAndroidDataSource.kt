package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToOneAndroidDataSource
import com.vgleadsheets.conversion.android.converter.SetlistEntryConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.SetlistEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.SetlistEntryEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.dao.SetlistEntryDataSource
import com.vgleadsheets.model.SetlistEntry
import com.vgleadsheets.model.Song

class SetlistEntryAndroidDataSource(
    private val convert: SetlistEntryConverter,
    private val foreignConverter: SongConverter,
    private val roomImpl: SetlistEntryRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToOneAndroidDataSource<
    SetlistEntryRoomDao,
    SetlistEntry,
    SetlistEntryEntity,
    Song,
    SongEntity,
    SongRoomDao,
    SetlistEntryConverter,
    SongConverter
    >(convert, foreignConverter, roomImpl, relatedRoomImpl),
    SetlistEntryDataSource {
    override fun getSetlistEntriesForJam(jamId: Long) = roomImpl
        .getSetlistEntriesForJam(jamId)
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                relatedRoomImpl,
                foreignConverter
            )
        }
}
