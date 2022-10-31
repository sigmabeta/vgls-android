package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToOneAndroidDataSource
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.SongHistoryEntryConverter
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry

class SongHistoryEntryAndroidDataSource(
    private val convert: SongHistoryEntryConverter,
    private val foreignConvert: SongConverter,
    private val roomImpl: SongHistoryEntryRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToOneAndroidDataSource<
    SongHistoryEntryRoomDao,
    SongHistoryEntry,
    SongHistoryEntryEntity,
    Song,
    SongEntity,
    SongRoomDao,
    SongHistoryEntryConverter,
    SongConverter
    >(convert, foreignConvert, roomImpl, relatedRoomImpl)
