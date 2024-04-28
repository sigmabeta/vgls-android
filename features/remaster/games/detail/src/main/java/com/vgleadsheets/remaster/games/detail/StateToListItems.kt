package com.vgleadsheets.remaster.games.detail

import android.content.res.Resources
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.games.detail.R
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun State.toListItems(resources: Resources): ImmutableList<ListModel> {
    val gameModels = if (game != null) {
        listOf<ListModel>(
            TitleListModel(
                title = game.name,
                subtitle = songs.captionText(resources),
                isLoading = false,
                shouldShowBack = false,
                allowExpansion = false,
                onMenuButtonClick = {},
                onImageLoadSuccess = {},
                onImageLoadFail = {},
            )
        )
    } else {
        emptyList()
    }

    val composerModels = composers.map { composer ->
        WideItemListModel(
            dataId = composer.id,
            name = composer.name,
            imageUrl = composer.photoUrl,
            imagePlaceholder = com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
            onClick = { }
        )
    }

    val songModels = songs.map { song ->
        ImageNameCaptionListModel(
            song.id,
            song.name,
            song.subtitleText(resources),
            song.thumbUrl(baseImageUrl, selectedPart),
            com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp
        ) { }
    }

    return (gameModels + composerModels + songModels).toPersistentList()
}


private fun Song.subtitleText(resources: Resources) = when (composers?.size) {
    null -> resources.getString(R.string.subtitle_composer_unknown)
    0 -> resources.getString(R.string.subtitle_composer_unknown)
    1 -> composers!!.first().name
    else -> resources.getString(R.string.subtitle_composer_various)
}

private fun Song.thumbUrl(baseImageUrl: String?, selectedPart: Part?): String? {
    return Page.generateImageUrl(
        baseImageUrl ?: return null,
        selectedPart?.apiId ?: return null,
        filename,
        isAltSelected,
        1
    )
}

private fun List<Song>.captionText(resources: Resources) = resources.getString(
    R.string.subtitle_sheets_count,
    size
)
