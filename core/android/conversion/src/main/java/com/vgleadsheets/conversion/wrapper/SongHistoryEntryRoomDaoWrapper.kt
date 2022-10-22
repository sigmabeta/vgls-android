package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.OneToOneWrapper
import com.vgleadsheets.conversion.converter.SongConverter
import com.vgleadsheets.conversion.converter.SongHistoryEntryConverter
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.enitity.SongHistoryEntryEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry

class SongHistoryEntryRoomDaoWrapper(
    private val convert: SongHistoryEntryConverter,
    private val foreignConvert: SongConverter,
    private val roomImpl: SongHistoryEntryRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToOneWrapper<
    SongHistoryEntryRoomDao,
    SongHistoryEntry,
    SongHistoryEntryEntity,
    Song,
    SongEntity,
    SongRoomDao,
    SongHistoryEntryConverter,
    SongConverter
    >(convert, foreignConvert, roomImpl, relatedRoomImpl)
