package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.OneToOneWrapper
import com.vgleadsheets.conversion.converter.SetlistEntryConverter
import com.vgleadsheets.conversion.converter.SongConverter
import com.vgleadsheets.database.android.dao.SetlistEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.SetlistEntryEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.model.SetlistEntry
import com.vgleadsheets.model.Song

class SetlistEntryRoomDaoWrapper(
    private val convert: SetlistEntryConverter,
    private val foreignConverter: SongConverter,
    private val roomImpl: SetlistEntryRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToOneWrapper<
    SetlistEntryRoomDao,
    SetlistEntry,
    SetlistEntryEntity,
    Song,
    SongEntity,
    SongRoomDao,
    SetlistEntryConverter,
    SongConverter
    >(convert, foreignConverter, roomImpl, relatedRoomImpl)
