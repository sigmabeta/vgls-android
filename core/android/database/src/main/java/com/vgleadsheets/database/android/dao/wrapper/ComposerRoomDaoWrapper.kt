package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.SongComposerRoomDao
import com.vgleadsheets.database.dao.ComposerDao
import com.vgleadsheets.database.enitity.ComposerEntity.Companion.toEntity
import com.vgleadsheets.model.Composer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ComposerRoomDaoWrapper(
    private val roomImpl: ComposerRoomDao,
    private val songComposerRoomImpl: SongComposerRoomDao
) : ComposerDao {
    override fun searchByName(name: String): Flow<List<Composer>> = roomImpl
        .searchByName(name)
        .map { list ->
            list.map { entity -> entity.toModel(false, songComposerRoomImpl) }
        }

    override fun getOneById(id: Long) = roomImpl
            .getOneById(id)
            .map { it.toModel(true, songComposerRoomImpl) }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .toModel(true, songComposerRoomImpl)

    override fun getAll(withRelated: Boolean): Flow<List<Composer>> = roomImpl
        .getAll()
        .map { list ->
            list.map { it.toModel(withRelated, songComposerRoomImpl) }
        }

    override suspend fun insert(models: List<Composer>) = roomImpl
        .insert(
            models.map {
                it.toEntity()
            }
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}

