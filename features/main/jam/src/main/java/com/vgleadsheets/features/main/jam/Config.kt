package com.vgleadsheets.features.main.jam

import android.content.res.Resources
import com.airbnb.mvrx.Loading
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.NetworkRefreshingListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.ListItemClicks.Companion.NO_ACTION
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.images.Page
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: JamState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig {
    private val jamLoad = state.contentLoad.jam

    private val jam = jamLoad.content()

    private val setlistLoad = state.contentLoad.setlist

    private val setlist = setlistLoad.content()

    private val songHistoryLoad = state.contentLoad.songHistory

    private val songHistory = songHistoryLoad.content()

    override val titleConfig = Title.Config(
        jam?.name ?: resources.getString(R.string.unknown_jam),
        resources.getString(R.string.subtitle_jam),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        allowExpansion = true,
        isLoading = jamLoad.isLoading()
    )

    override val actionsConfig = Actions.Config(
        jamLoad.isReady(),
        listOf(
            CtaListModel(
                R.drawable.ic_playlist_play_black_24dp,
                resources.getString(R.string.cta_follow_jam)
            ) { clicks.follow(state.jamId) },
            CtaListModel(
                R.drawable.ic_refresh_24dp,
                resources.getString(R.string.cta_refresh_jam),
            ) { clicks.refresh() },
            CtaListModel(
                R.drawable.ic_delete_black_24dp,
                resources.getString(R.string.cta_delete_jam)
            ) { clicks.delete() }
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
                        Page.generateThumbUrl(
                            baseImageUrl,
                            hudState.selectedPart.apiId,
                            currentSong.filename
                        ),
                        R.drawable.placeholder_sheet
                    ) { clicks.song(currentSong.id) }
                )
            }
        }
    }

    private suspend fun setlistSection(): List<ListModel> {
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
            sectionTitle + setlist.mapYielding {
                val song = it.song
                val dataId = it.id + ID_OFFSET_SETLIST

                ImageNameCaptionListModel(
                    dataId,
                    song?.name ?: return@mapYielding songLoadError(dataId),
                    song.gameName,
                    Page.generateThumbUrl(
                        baseImageUrl,
                        hudState.selectedPart.apiId,
                        song.filename
                    ),
                    R.drawable.placeholder_sheet
                ) { clicks.song(song.id) }
            }
        }
    }

    private suspend fun historySection(): List<ListModel> {
        val sectionTitle = listOf(
            SectionHeaderListModel(
                resources.getString(R.string.section_song_history)
            )
        )

        return sectionTitle + if (state.contentLoad.setlistRefresh is Loading) {
            listOf(NetworkRefreshingListModel("history"))
        } else if (jamLoad.isLoading()) {
            buildList {
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
            songHistory?.mapYielding {
                val song = it.song
                val dataId = it.id + ID_OFFSET_SONG_HISTORY

                ImageNameCaptionListModel(
                    dataId,
                    song?.name ?: return@mapYielding songLoadError(dataId),
                    song.gameName,
                    Page.generateThumbUrl(
                        baseImageUrl,
                        hudState.selectedPart.apiId,
                        song.filename
                    ),
                    R.drawable.placeholder_sheet
                ) { clicks.song(song.id) }
            } ?: emptyList()
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
        JamFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        false,
        LoadingItemStyle.WITH_IMAGE
    )

    private fun songLoadError(id: Long) = ImageNameCaptionListModel(
        id,
        resources.getString(R.string.unknown_song),
        resources.getString(R.string.error_song),
        null,
        R.drawable.ic_error_24dp,
        onClick = NO_ACTION
    )

    companion object {
        private const val ID_OFFSET_SETLIST = 1_000L
        private const val ID_OFFSET_SONG_HISTORY = 1_000_000L
    }
}
