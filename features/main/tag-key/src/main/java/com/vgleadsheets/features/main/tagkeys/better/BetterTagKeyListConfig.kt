package com.vgleadsheets.features.main.tagkeys.better

import android.content.res.Resources
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.isNullOrEmpty
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.features.main.tagkeys.BuildConfig
import com.vgleadsheets.features.main.tagkeys.R
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class BetterTagKeyListConfig(
    private val state: BetterTagKeyListState,
    private val hudState: HudState,
    private val viewModel: BetterTagKeyListViewModel,
    private val clicks: BetterTagKeyListClicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterTagKeyListState, BetterTagKeyListClicks> {
    override val titleConfig = Title.Config(
        resources.getString(R.string.app_name),
        resources.getString(R.string.label_by_tag),
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
        state.contentLoad.content()
            ?.map {
                NameCaptionListModel(
                    it.id,
                    it.name,
                    it.captionText()
                ) { viewModel.onTagKeyClicked(it.id, it.name) }
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_tag_Key)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterTagKeyListFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.REGULAR
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun TagKey.captionText(): String {
        val items = values // To avoid repetitive nullchecking
        if (items.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = items.size

        while (builder.length < ListViewModel.MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= ListViewModel.MAX_LENGTH_SUBTITLE_ITEMS) {
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

        return builder.toString()
    }
}
