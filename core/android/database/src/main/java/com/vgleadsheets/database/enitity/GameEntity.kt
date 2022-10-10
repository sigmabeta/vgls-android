package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.nullWhenFalse
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import kotlinx.coroutines.flow.map

@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val hasVocalSongs: Boolean,
    val photoUrl: String? = null
) : OneToManyEntity<Game, GameEntity, Song, SongRoomDao> {
    override fun toModel(withRelatedModels: Boolean, relatedDao: SongRoomDao) = Game(
        id,
        name,
        withRelatedModels.nullWhenFalse { relatedDao.getRelatedModels(id) },
        photoUrl
    )

    override fun SongRoomDao.getRelatedModels(relationId: Long) = getSongsForGameSync(id)
        .map { songEntity -> songEntity.toSong(null)  }

    override fun converter(): ModelToEntityConverter<GameEntity, Game> = Companion

    companion object : ModelToEntityConverter<GameEntity, Game> {
        override fun Game.toEntity() = GameEntity(
            id,
            name,
            songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
            photoUrl
        )
    }
}
