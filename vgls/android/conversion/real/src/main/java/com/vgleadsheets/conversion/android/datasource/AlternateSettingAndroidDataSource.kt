package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.database.android.dao.AlternateSettingRoomDao
import com.vgleadsheets.database.source.AlternateSettingDataSource
import kotlinx.coroutines.flow.map

class AlternateSettingAndroidDataSource(
    private val roomImpl: AlternateSettingRoomDao,
) : AlternateSettingDataSource {
    override suspend fun toggleAlternate(id: Long) {
        roomImpl.toggleAlternate(id)
    }

    override fun isAlternateSelected(id: Long) = roomImpl
        .getAlternateSetting(id)
        .map { it?.isAltSelected == true }
}
