package com.vgleadsheets.remaster.tags.songs

import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.ImageNameCaptionListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.list.ListState
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider

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
                        pdfSize = PdfSize.THUMBNAIL,
                    )
                ),
                imagePlaceholder = Icon.DESCRIPTION,
                clickAction = Action.SongClicked(song.id),
            )
        }
    }
}
