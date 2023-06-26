package com.vgleadsheets.features.main.composers

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.list.ListConfig
import com.vgleadsheets.features.main.list.ListConfig.Companion.MAX_LENGTH_SUBTITLE_CHARS
import com.vgleadsheets.features.main.list.ListConfig.Companion.MAX_LENGTH_SUBTITLE_ITEMS
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: ComposerListState,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : ListConfig {
    override val titleConfig = Title.Config(
        resources.getString(com.vgleadsheets.ui_core.R.string.app_name),
        resources.getString(com.vgleadsheets.features.main.hud.R.string.label_by_composer),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        onMenuButtonClick = { clicks.menu() }
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !state.composers().isNullOrEmpty()
    ) {
        val composers = state.composers()

        if (composers.isNullOrEmpty()) {
            return@Config listOf(
                EmptyStateListModel(
                    com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
                    resources.getString(com.vgleadsheets.features.main.list.R.string.empty_transposition),
                )
            )
        }

        val onlyTheHits = composers
            .filter { it.isFavorite }
            .mapYielding {
                ImageNameCaptionListModel(
                    it.id + ListConfig.OFFSET_FAVORITE,
                    it.name,
                    captionText(it, state.composerToSongListMap),
                    it.photoUrl,
                    com.vgleadsheets.vectors.R.drawable.ic_person_24dp
                ) { clicks.composer(it.id) }
            }

        val filteredComposerItems = composers.mapYielding {
            ImageNameCaptionListModel(
                it.id,
                it.name,
                captionText(it, state.composerToSongListMap),
                it.photoUrl,
                com.vgleadsheets.vectors.R.drawable.ic_person_24dp
            ) { clicks.composer(it.id) }
        }

        val favoriteSection = if (onlyTheHits.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    resources.getString(com.vgleadsheets.features.main.list.R.string.section_header_favorites)
                )
            ) + onlyTheHits
        } else {
            emptyList()
        }

        val restOfThem = if (favoriteSection.isEmpty()) {
            emptyList()
        } else {
            listOf(
                SectionHeaderListModel(
                    resources.getString(R.string.section_header_all_composers)
                )
            )
        } + filteredComposerItems

        return@Config favoriteSection + restOfThem
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        com.vgleadsheets.vectors.R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_composer)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        ComposerListFragment.LOAD_OPERATION,
        state.failure()?.message
            ?: resources.getString(com.vgleadsheets.features.main.list.R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun captionText(
        composer: Composer,
        composerToSongListMap: Async<Map<Composer, List<Song>>>
    ): String {
        return when (composerToSongListMap) {
            is Fail -> "Error: no values found."
            is Uninitialized, is Loading -> "Now loading, please wait..."
            is Success -> {
                val items = composerToSongListMap()[composer]
                if (items.isNullOrEmpty()) return "Error: no values found."

                val builder = StringBuilder()
                var numberOfOthers = items.size

                while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
                    val index = items.size - numberOfOthers

                    if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                        break
                    }

                    if (numberOfOthers == 0) {
                        break
                    }

                    if (index != 0) {
                        builder.append(resources.getString(R.string.subtitle_separator))
                    }

                    val stringToAppend = items[index].name
                    builder.append(stringToAppend)
                    numberOfOthers--
                }

                if (numberOfOthers != 0) {
                    builder.append(
                        resources.getString(R.string.subtitle_suffix_others, numberOfOthers)
                    )
                }

                builder.toString()
            }
        }

    }
}
