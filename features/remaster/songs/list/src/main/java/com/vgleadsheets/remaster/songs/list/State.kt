package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val songs: LCE<List<Song>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_ALL)
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
