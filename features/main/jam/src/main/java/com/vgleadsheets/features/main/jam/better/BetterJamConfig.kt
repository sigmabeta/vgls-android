package com.vgleadsheets.features.main.jam.better

import android.content.res.Resources
import com.airbnb.mvrx.Loading
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.NetworkRefreshingListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.jam.BuildConfig
import com.vgleadsheets.features.main.jam.R
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker

class BetterJamConfig(
    private val state: BetterJamState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val viewModel: BetterJamViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterJamState, BetterJamClicks> {
    private val jamLoad = state.contentLoad.jam

    private val jam = jamLoad.content()

    private val setlistLoad = state.contentLoad.setlist

    private val setlist = setlistLoad.content()

    override val titleConfig = Title.Config(
        jam?.name ?: resources.getString(R.string.unknown_jam),
        resources.getString(R.string.subtitle_jam),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        shouldShow = true,
        isLoading = jamLoad.isLoading()
    )

    override val actionsConfig = Actions.Config(
        jamLoad.isReady(),
        listOf(
            CtaListModel(
                R.drawable.ic_playlist_play_black_24dp,
                resources.getString(R.string.cta_follow_jam),
                onFollowClicked()
            ),
            CtaListModel(
                R.drawable.ic_refresh_24dp,
                resources.getString(R.string.cta_refresh_jam),
                onRefreshClicked()
            ),
            CtaListModel(
                R.drawable.ic_delete_black_24dp,
                resources.getString(R.string.cta_delete_jam),
                onDeleteClicked()
            )
        )
    )

    override val contentConfig = Content.Config(
        true
    ) {
        currentSongSection() + setlistSection() + historySection()
    }

    private fun currentSongSection(): List<ListModel> {
        val sectionTitle = listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_current_song)
            )
        )

        return if (state.contentLoad.setlistRefresh is Loading) {
            sectionTitle + listOf(NetworkRefreshingListModel("currentSong"))
        } else if (jamLoad.isLoading()) {
            sectionTitle + listOf(
                LoadingImageNameCaptionListModel(
                    "currentSong",
                    0
                )
            )
        } else {
            val currentSong = jam?.currentSong

            if (currentSong == null) {
                emptyList()
            } else {
                sectionTitle + listOf(
                    ImageNameCaptionListModel(
                        currentSong.id,
                        currentSong.name,
                        currentSong.gameName,
                        Page.generateImageUrl(
                            baseImageUrl,
                            hudState.selectedPart,
                            currentSong.filename,
                            1
                        ),
                        R.drawable.placeholder_sheet,
                        onSongClicked()
                    )
                )
            }
        }
    }

    private fun setlistSection(): List<ListModel> {
        val sectionTitle = listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_setlist)
            )
        )
        return if (state.contentLoad.setlistRefresh is Loading) {
            sectionTitle + listOf(NetworkRefreshingListModel("setlist"))
        } else if (setlistLoad.isLoading()) {
            sectionTitle + buildList<ListModel> {
                repeat(2) {
                    add(
                        LoadingImageNameCaptionListModel(
                            "setlist",
                            it
                        )
                    )
                }
            }
        } else if (setlist.isNullOrEmpty()) {
            emptyList()
        } else {
            sectionTitle + setlist.map {
                ImageNameCaptionListModel(
                    it.id,
                    it.songName,
                    it.gameName,
                    Page.generateImageUrl(
                        baseImageUrl,
                        hudState.selectedPart,
                        it.song?.filename ?: return@map songLoadError(),
                        1
                    ),
                    R.drawable.placeholder_sheet,
                    onSongClicked()
                )
            }
        }
    }

    private fun historySection(): List<ListModel> {
        val sectionTitle = listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_song_history)
            )
        )

        return if (state.contentLoad.setlistRefresh is Loading) {
            sectionTitle + listOf(NetworkRefreshingListModel("history"))
        } else if (jamLoad.isLoading()) {
            sectionTitle + buildList<ListModel> {
                repeat(2) {
                    add(
                        LoadingImageNameCaptionListModel(
                            "jamHistory",
                            it
                        )
                    )
                }
            }
        } else {
            val songHistory = jam?.songHistory

            if (songHistory == null) {
                emptyList()
            } else {
                sectionTitle + songHistory.map {
                    ImageNameCaptionListModel(
                        it.id,
                        it.song?.name ?: return@map songLoadError(),
                        it.song?.gameName ?: return@map songLoadError(),
                        Page.generateImageUrl(
                            baseImageUrl,
                            hudState.selectedPart,
                            it.song?.filename ?: return@map songLoadError(),
                            1
                        ),
                        R.drawable.placeholder_sheet,
                        onSongClicked()
                    )
                }
            }
        }
    }

    override val emptyConfig = EmptyState.Config(
        false,
        0,
        ""
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterJamFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        false,
        LoadingItemStyle.WITH_IMAGE
    )

    private fun onFollowClicked() = object : CtaListModel.EventHandler {
        override fun onClicked(clicked: CtaListModel) {
            viewModel.onFollowClicked()
        }

        override fun clearClicked() {}
    }

    private fun onRefreshClicked() = object : CtaListModel.EventHandler {
        override fun onClicked(clicked: CtaListModel) {
            viewModel.refreshJam()
        }

        override fun clearClicked() {}
    }

    private fun onDeleteClicked() = object : CtaListModel.EventHandler {
        override fun onClicked(clicked: CtaListModel) {
            viewModel.deleteJam()
        }

        override fun clearClicked() {}
    }

    private fun onSongClicked() =
        object : ImageNameCaptionListModel.EventHandler {
            override fun onClicked(clicked: ImageNameCaptionListModel) {
                viewModel.onSongClicked(
                    clicked.dataId,
                    clicked.name,
                    setlistLoad
                        .content()
                        ?.firstOrNull { it.id == clicked.dataId }
                        ?.gameName
                        ?: "",
                    hudState.selectedPart.apiId
                )
            }

            override fun clearClicked() {}
        }

    private fun songLoadError() = ImageNameCaptionListModel(
        -1L,
        resources.getString(R.string.unknown_song),
        resources.getString(R.string.error_song),
        null,
        R.drawable.ic_error_24dp,
        onSongClicked(),
        null,
    )
}
