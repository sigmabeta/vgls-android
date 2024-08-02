package com.vgleadsheets.remaster.tags.songs

import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val tagValue: TagValue? = null,
    val songs: List<Song> = emptyList(),
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = if (tagValue != null) {
            stringProvider.getStringTwoArgs(StringId.SCREEN_TITLE_BROWSE_SONGS_WITH_TAG, tagValue.tagKeyName, tagValue.name)
        } else {
            stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
        }
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return songs
            .map { song ->
                ImageNameCaptionListModel(
                    dataId = song.id,
                    name = song.name,
                    caption = song.gameName,
                    sourceInfo = PdfConfigById(
                        songId = song.id,
                        pageNumber = 0
                    ),
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.SongClicked(song.id),
                )
            }
            .toImmutableList()
    }
}
