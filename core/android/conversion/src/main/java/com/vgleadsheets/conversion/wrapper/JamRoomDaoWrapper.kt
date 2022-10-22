package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.OneToManyWrapper
import com.vgleadsheets.conversion.converter.JamConverter
import com.vgleadsheets.conversion.converter.SongConverter
import com.vgleadsheets.conversion.converter.SongHistoryEntryConverter
import com.vgleadsheets.database.android.dao.JamRoomDao
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.JamEntity
import com.vgleadsheets.database.enitity.SongHistoryEntryEntity
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.SongHistoryEntry
import kotlinx.coroutines.flow.map

class JamRoomDaoWrapper(
    private val convert: JamConverter,
    private val roomImpl: JamRoomDao,
    private val otoRelatedRoomImpl: SongRoomDao,
    private val otmRelatedRoomImpl: SongHistoryEntryRoomDao,
    private val songConverter: SongConverter,
    private val songHistoryEntryConverter: SongHistoryEntryConverter
) : OneToManyWrapper<JamRoomDao, Jam, JamEntity, SongHistoryEntry, SongHistoryEntryEntity, SongHistoryEntryRoomDao, JamConverter, SongHistoryEntryConverter>(
    convert,
    songHistoryEntryConverter,
    roomImpl,
    otmRelatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map {
            convert.toModelFull(
                it,
                otoRelatedRoomImpl,
                otmRelatedRoomImpl,
                songConverter,
                songHistoryEntryConverter
            )
        }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let {
            convert.toModelFull(
                it,
                otoRelatedRoomImpl,
                otmRelatedRoomImpl,
                songConverter,
                songHistoryEntryConverter
            )
        }
}
