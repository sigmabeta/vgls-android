package com.vgleadsheets.features.main.composers

import android.content.res.Resources
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.features.main.list.ListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
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
        resources.getString(com.vgleadsheets.ui.strings.R.string.app_name),
        resources.getString(R.string.subtitle_composer),
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
                    com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
                    resources.getString(com.vgleadsheets.features.main.list.R.string.empty_transposition),
                )
            )
        }

        val onlyTheHits = composers
            .filter { it.isFavorite }
            .mapYielding {
                WideItemListModel(
                    it.id + ListConfig.OFFSET_FAVORITE,
                    it.name,
                    it.photoUrl,
                    com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp
                ) { clicks.composer(it.id) }
            }

        val filteredComposerItems = composers.mapYielding {
            WideItemListModel(
                it.id,
                it.name,
                it.photoUrl,
                com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp
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
        com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
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
}
