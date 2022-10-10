package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.joins.SongTagValueJoin
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

class SongTagValueRoomDaoWrapper(
    private val roomImpl: RoomDao
) : Dao {

    override suspend fun insertAll(songTagValueJoins: List<SongTagValueJoin>)

    """
            SELECT * FROM tag_value INNER JOIN song_tag_value_join 
            ON tag_value.id=song_tag_value_join.tagValueId
            WHERE song_tag_value_join.songId=:songId
            ORDER BY tag_key_name
            COLLATE NOCASE
            """
    )
    override fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>

    """
            SELECT * FROM song INNER JOIN song_tag_value_join 
            ON song.id=song_tag_value_join.songId
            WHERE song_tag_value_join.tagValueId=:tagValueId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    override fun getSongsForTagValueSync(tagValueId: Long): List<Song>

    """
            SELECT * FROM song INNER JOIN song_tag_value_join 
            ON song.id=song_tag_value_join.songId
            WHERE song_tag_value_join.tagValueId=:tagValueId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    override fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>

    override suspend fun nukeTable()
}
