package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.SongComposerRoomDao
import com.vgleadsheets.database.nullWhenFalse
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals

@Entity(
    tableName = "composer"
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String,
    var hasVocalSongs: Boolean = false,
    val photoUrl: String? = null
) : OneToManyEntity<Composer, ComposerEntity, Song, SongComposerRoomDao> {
    override fun toModel(withRelatedModels: Boolean, relatedDao: SongComposerRoomDao) = Composer(
        id,
        name,
        withRelatedModels.nullWhenFalse { relatedDao.getRelatedModels(id) },
        photoUrl
    )

    override fun SongComposerRoomDao.getRelatedModels(relationId: Long) =
        getSongsForComposerSync(relationId)
            .map { songEntity -> songEntity.toSong(null) }

    override fun converter() = Companion

    companion object: ModelToEntityConverter<ComposerEntity, Composer> {
        override fun Composer.toEntity() = ComposerEntity(
            id,
            name,
            songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
            photoUrl
        )
    }
}
