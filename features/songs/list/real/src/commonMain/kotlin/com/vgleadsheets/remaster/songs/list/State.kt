package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.model.Song
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.ImageNameCaptionListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.list.PaginationType
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val songs: LCE<List<Song>> = LCE.Uninitialized,
    // Paging window: [windowStart, windowStart + songs.size) within the name-ordered catalog.
    val windowStart: Int = 0,
    val hasMoreBefore: Boolean = false,
    val hasMoreAfter: Boolean = true,
    val loadingPrevious: Boolean = false,
    val loadingMore: Boolean = false,
) : ListState() {
    override val paginationType = PaginationType.Standard()

    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_BROWSE_ALL)
    )

    @Suppress("MagicNumber")
    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val content = songs.withStandardErrorAndLoading(
            loadingType = LoadingType.TEXT_CAPTION_IMAGE,
            loadingWithHeader = false,
        ) {
            content(data)
        }
        // Inline header/footer spinners while a page is loading above or below the window.
        return pageLoader(loadingPrevious, LOAD_PREVIOUS_OP) + content + pageLoader(loadingMore, LOAD_MORE_OP)
    }

    private fun pageLoader(show: Boolean, operationName: String): List<ListModel> {
        if (!show) return emptyList()
        val type = paginationType as? PaginationType.Paginating ?: return emptyList()
        return listOf(
            LoadingItemListModel(
                loadingType = type.loadingType,
                loadOperationName = operationName,
                loadPositionOffset = 0,
            )
        )
    }

    private fun content(songs: List<Song>) = songs.map { song ->
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
            imagePlaceholder = Icon.Description,
            clickAction = Action.SongClicked(song.id),
        )
    }

    private companion object {
        const val LOAD_PREVIOUS_OP = "songs.list.load_previous"
        const val LOAD_MORE_OP = "songs.list.load_more"
    }
}
