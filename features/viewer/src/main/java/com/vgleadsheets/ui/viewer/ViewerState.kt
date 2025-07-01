package com.vgleadsheets.ui.viewer

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.images.PdfSize
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class ViewerState(
    val song: LCE<Song> = LCE.Uninitialized,
    val partApiId: String? = null,
    val initialPage: Int = 0,
    val buttonsVisible: Boolean = true,
    val keepScreenOn: Boolean? = null,
    val isZoomedIn: Boolean = false,
    val isAltSelected: LCE<Boolean> = LCE.Uninitialized,
    val isSongHistoryEntryRecorded: Boolean = false,
) : VglsState {
    fun title(stringProvider: StringProvider): TitleBarModel {
        return if (song is LCE.Content) {
            val gameName = song.data.gameName
            TitleBarModel(
                title = song.data.name,
                subtitle = stringProvider.getStringOneArg(StringId.SCREEN_SUBTITLE_SONG_DETAIL, gameName),
            )
        } else {
            TitleBarModel()
        }
    }

    fun pages(): ImmutableList<ZoomableSheetPageListModel> {
        return if (song is LCE.Content && partApiId != null) {
            val pageCount = song.data.pageCount(partApiId, false)
            val actualPartApiId = if (pageCount > 0) {
                partApiId
            } else {
                Part.C.apiId
            }

            val (actualPageCount, altSelection) = if (isAltSelected !is LCE.Content) {
                0 to false
            } else {
                song.data.pageCount(actualPartApiId, isAltSelected.data) to isAltSelected.data
            }

            val singlePage = if (actualPageCount == 1) 0 else null

            if (singlePage != null) {
                persistentListOf(
                    ZoomableSheetPageListModel(
                        pdfConfigById = PdfConfigById(
                            songId = song.data.id,
                            pageNumber = singlePage,
                            isAltSelected = altSelection,
                            pdfSize = PdfSize.FILL,
                        ),
                        title = song.data.name,
                        gameName = song.data.gameName,
                        composers = song.data.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
                        pageNumber = singlePage,
                        clickAction = VglsAction.Noop,
                    )
                )
            } else {
                List(actualPageCount) { pageNumber ->
                    ZoomableSheetPageListModel(
                        pdfConfigById = PdfConfigById(
                            songId = song.data.id,
                            pageNumber = pageNumber,
                            isAltSelected = altSelection,
                            pdfSize = PdfSize.FILL,
                        ),
                        title = song.data.name,
                        gameName = song.data.gameName,
                        composers = song.data.composers?.map { it.name }?.toImmutableList() ?: persistentListOf(),
                        pageNumber = pageNumber,
                        clickAction = VglsAction.Noop,
                    )
                }.toImmutableList()
            }
        } else {
            persistentListOf()
        }
    }

    fun error(): ImmutableList<ErrorStateListModel> = if (song is LCE.Error) {
        persistentListOf(
            ErrorStateListModel(
                failedOperationName = song.operationName,
                errorString = "Could not find this song. Try again later.",
                error = song.error
            )
        )
    } else {
        persistentListOf()
    }

    fun shouldShowLyricsWarning(): Boolean {
        return if (song is LCE.Content && partApiId != null) {
            song.data.pageCount(partApiId, false) <= 0
        } else {
            false
        }
    }
}
