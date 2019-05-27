package com.vgleadsheets.model.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.ListItem

@Entity(tableName = "game")
data class GameEntity(
    val name: String
) : ListItem<GameEntity> {
    @PrimaryKey(autoGenerate = true) var id: Long? = null

    override fun isTheSameAs(theOther: GameEntity?) = name == theOther?.name

    override fun hasSameContentAs(theOther: GameEntity?) = name == theOther?.name /*&& songs.size == theOther.songs.size*/

    override fun getChangeType(theOther: GameEntity?) = ListItem.CHANGE_ERROR
}