package com.vgleadsheets.remaster.tags.songs

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val tagValue: LCE<TagValue> = LCE.Uninitialized,
    val songs: LCE<List<Song>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = if (tagValue is LCE.Content) {
            stringProvider.getStringTwoArgs(
                StringId.SCREEN_TITLE_BROWSE_SONGS_WITH_TAG,
                tagValue.data.tagKeyName,
                tagValue.data.name
            )
        } else {
            stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
        }
    )

    @Suppress("MagicNumber")
    override fun toListItems(stringProvider: StringProvider) = songs.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_CAPTION_IMAGE,
        loadingWithHeader = false,
    ) {
        data.map { song ->
            ImageNameCaptionListModel(
                dataId = song.id,
                name = song.name,
                caption = song.gameName,
                sourceInfo = SourceInfo(
                    PdfConfigById(
                        songId = song.id,
                        isAltSelected = false,
                        pageNumber = 0,
                    )
                ),
                imagePlaceholder = Icon.DESCRIPTION,
                clickAction = Action.SongClicked(song.id),
            )
        }
    }
}
