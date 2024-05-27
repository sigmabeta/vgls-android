package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.images.Page
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Song
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val songs: List<Song> = emptyList(),
    val sheetUrlInfo: UrlInfo = UrlInfo(),
) : ListState() {
    override val renderAsGrid = true
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_ALL)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        println(sheetUrlInfo)
        return songs
            .map { song ->
                ImageNameCaptionListModel(
                    dataId = song.id,
                    name = song.name,
                    caption = song.gameName,
                    imageUrl = song.thumbUrl(sheetUrlInfo.imageBaseUrl, sheetUrlInfo.partId),
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.SongClicked(song.id),
                )
            }
            .toImmutableList()
    }

    private fun Song.thumbUrl(baseImageUrl: String?, selectedPart: String?): String? {
        return Page.generateImageUrl(
            baseImageUrl = baseImageUrl ?: return null,
            partApiId = selectedPart ?: return null,
            filename = filename,
            isAlternateEnabled = isAltSelected,
            pageNumber = 1
        )
    }
}