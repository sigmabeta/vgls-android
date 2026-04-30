package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.OfflineUpdateResultEntity
import com.vgleadsheets.model.updates.OfflineJobStatus
import com.vgleadsheets.model.updates.OfflineUpdateResult
import org.threeten.bp.ZonedDateTime

class OfflineUpdateResultConverter : Converter<OfflineUpdateResult, OfflineUpdateResultEntity> {
    override fun OfflineUpdateResult.toEntity() = OfflineUpdateResultEntity(
        date_time = dateTime.toString(),
        server_update_time = serverUpdateTime.toString(),
        updated_songs = updatedSongs,
        successful_offlines = successfulOfflines,
        status = status.name,
    )

    override fun OfflineUpdateResultEntity.toModel() = OfflineUpdateResult(
        id = id.toInt(),
        dateTime = ZonedDateTime.parse(date_time),
        serverUpdateTime = ZonedDateTime.parse(server_update_time),
        updatedSongs = updated_songs,
        successfulOfflines = successful_offlines,
        status = OfflineJobStatus.valueOf(status),
    )
}
