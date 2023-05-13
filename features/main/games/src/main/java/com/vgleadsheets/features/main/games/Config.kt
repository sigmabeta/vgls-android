package com.vgleadsheets.features.main.games

import android.content.res.Resources
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.MAX_LENGTH_SUBTITLE_CHARS
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.MAX_LENGTH_SUBTITLE_ITEMS
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.OFFSET_FAVORITE
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.isNullOrEmpty
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: GameListState,
    private val hudState: HudState,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig {
    override val titleConfig = Title.Config(
        resources.getString(R.string.app_name),
        resources.getString(R.string.label_by_game),
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
        val filteredGames = state.contentLoad.content()
            ?.filter { !it.songs?.filteredForVocals(hudState.selectedPart.apiId).isNullOrEmpty() }

        if (filteredGames.isNullOrEmpty()) {
            return@Config listOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    resources.getString(R.string.empty_transposition),
                )
            )
        }

        val onlyTheHits = filteredGames
            .filter { it.isFavorite }
            .mapYielding {
                ImageNameCaptionListModel(
                    it.id + OFFSET_FAVORITE,
                    it.name,
                    it.captionText(),
                    it.photoUrl,
                    R.drawable.ic_album_24dp
                ) { clicks.game(it.id) }
            }

        val filteredGameItems = filteredGames.mapYielding {
            ImageNameCaptionListModel(
                it.id,
                it.name,
                it.captionText(),
                it.photoUrl,
                R.drawable.placeholder_game
            ) { clicks.game(it.id) }
        }

        val favoriteSection = if (onlyTheHits.isNotEmpty()) {
            listOf(
                SectionHeaderListModel(
                    resources.getString(R.string.section_header_favorites)
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
                    resources.getString(R.string.section_header_all_games)
                )

            )
        } + filteredGameItems

        return@Config favoriteSection + restOfThem
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_game)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        GameListFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun Game.captionText(): String {
        val items = songs
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

        return builder.toString()
    }
}
