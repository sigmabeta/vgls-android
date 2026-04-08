package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.OfflineUpdateResultEntity
import com.vgleadsheets.model.updates.OfflineUpdateResult
import org.threeten.bp.ZonedDateTime

class OfflineUpdateResultConverter : Converter<OfflineUpdateResult, OfflineUpdateResultEntity> {
    override fun OfflineUpdateResult.toEntity() = OfflineUpdateResultEntity(
        id = id.toLong(),
        date_time = dateTime.toString(),
        server_update_time = serverUpdateTime.toString(),
        new_songs = newSongs,
        new_games = newGames,
        new_composers = newComposers,
        updated_songs = updatedSongs,
        successful_offlines = successfulOfflines,
    )

    override fun OfflineUpdateResultEntity.toModel() = OfflineUpdateResult(
        id = id.toInt(),
        dateTime = ZonedDateTime.parse(date_time),
        serverUpdateTime = ZonedDateTime.parse(server_update_time),
        newSongs = new_songs,
        newGames = new_games,
        newComposers = new_composers,
        updatedSongs = updated_songs,
        successfulOfflines = successful_offlines,
    )
}
