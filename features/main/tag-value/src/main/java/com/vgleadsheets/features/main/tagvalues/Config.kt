package com.vgleadsheets.features.main.tagvalues

import android.content.res.Resources
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.MAX_LENGTH_SUBTITLE_CHARS
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.MAX_LENGTH_SUBTITLE_ITEMS
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: TagValueState,
    private val hudState: HudState,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources,
    private val hatchet: Hatchet,
) : BetterListConfig {
    private val tagKeyLoad = state.contentLoad.tagKey

    private val tagKey = tagKeyLoad.content()

    private val tagValueLoad = state.contentLoad.tagValues

    private val tagValues = tagValueLoad.content()

    override val titleConfig = Title.Config(
        tagKey?.name ?: resources.getString(R.string.unknown_tag_key),
        tagValues?.subtitleText(),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        allowExpansion = true,
        isLoading = tagKeyLoad.isLoading()
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !tagValues.isNullOrEmpty()
    ) {
        hatchet.w(this.javaClass.simpleName, "Tag Values: ${tagValues?.size}")
        tagValues
            ?.filter { !it.songs?.filteredForVocals(hudState.selectedPart.apiId).isNullOrEmpty() }
            ?.mapYielding {
                NameCaptionListModel(
                    it.id,
                    it.name,
                    it.songs
                        ?.filteredForVocals(hudState.selectedPart.apiId)
                        ?.captionText() ?: "Error: no values found."
                ) { clicks.tagValue(it.id) }
            } ?: emptyList()
    }

    override val emptyConfig = EmptyState.Config(
        tagValues?.isEmpty() == true,
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_tag_value_key)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        TagValueFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun List<Song>.captionText(): String {
        val items = this
        if (items.isEmpty()) return "Error: no values found."

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

        return builder.toString()
    }

    private fun List<TagValue>.subtitleText() = resources.getString(
        R.string.subtitle_options_count,
        size
    )
}
