package com.vgleadsheets.features.main.jams

import android.content.res.Resources
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.isNullOrEmpty
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class Config(
    private val state: JamListState,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<JamListState, Clicks> {
    override val titleConfig = Title.Config(
        resources.getString(R.string.app_name),
        resources.getString(R.string.label_jams),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { }
    )

    override val actionsConfig = Actions.Config(
        true,
        listOf(
            CtaListModel(
                R.drawable.ic_add_black_24dp,
                resources.getString(R.string.cta_find_jam)
            ) { clicks.findJam() }
        )
    )

    override val contentConfig = Content.Config(
        !state.contentLoad.isNullOrEmpty()
    ) {
        state.contentLoad
            .content()
            ?.map {
                NameCaptionListModel(
                    it.id,
                    it.name,
                    it.captionText()
                ) { clicks.jam(it.id) }
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        0,
        ""
    ) {
        listOf(
            EmptyStateListModel(
                R.drawable.ic_list_black_24dp,
                resources.getString(R.string.empty_jams)
            )
        )
    }

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        JamListFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun Jam.captionText() = resources.getString(
        R.string.caption_jam,
        currentSong?.name ?: "Unknown Song",
        currentSong?.gameName ?: "Unknown Game"
    )
}
