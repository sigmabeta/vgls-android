package com.vgleadsheets.features.main.jams.better

import android.content.res.Resources
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.jams.BuildConfig
import com.vgleadsheets.features.main.jams.R
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

class BetterJamListConfig(
    private val state: BetterJamListState,
    private val hudState: HudState,
    private val viewModel: BetterJamListViewModel,
    private val clicks: BetterJamListClicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterJamListState, BetterJamListClicks> {
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

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !state.contentLoad.isNullOrEmpty()
    ) {
        listOf(
            CtaListModel(
                R.drawable.ic_add_black_24dp,
                resources.getString(R.string.cta_find_jam),
                onFindJamClicked(clicks)
            )
        ) + (
            state.contentLoad
                .content()
                ?.map {
                    NameCaptionListModel(
                        it.id,
                        it.name,
                        it.captionText(),
                        onJamClicked(clicks)
                    )
                } ?: emptyList()
            )
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
        BetterJamListFragment.LOAD_OPERATION,
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

    private fun onFindJamClicked(clicks: BetterJamListClicks) =
        object : CtaListModel.EventHandler {
            override fun onClicked(clicked: CtaListModel) {
                clicks.onFindJamClicked(viewModel)
            }

            override fun clearClicked() {}
        }

    private fun onJamClicked(clicks: BetterJamListClicks) =
        object : NameCaptionListModel.EventHandler {
            override fun onClicked(clicked: NameCaptionListModel) {
                clicks.onJamClicked(clicked.dataId, clicked.name, viewModel)
            }

            override fun clearClicked() {}
        }
}
