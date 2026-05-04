package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.model.Song
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.ImageNameCaptionListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val songs: LCE<List<Song>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_BROWSE_ALL)
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
