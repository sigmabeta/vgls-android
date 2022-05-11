package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SearchFragment : AsyncListFragment<SearchData, SearchState>() {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    override val viewModel: SearchViewModel by fragmentViewModel()

    override fun getTrackingScreen() = TrackingScreen.SEARCH

    @SuppressWarnings(
        "ThrowingExceptionsWithoutMessageOrCause",
        "EmptyCatchBlock",
        "SwallowedException"
    )
    override fun getDetails(): String {
        var query = ""

        try {
            withState(viewModel) { query = it.data.query ?: "" }
        } catch (ex: IllegalStateException) { }

        return query
    }

    override fun getPerfScreenName() = getTrackingScreen().toString()

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.SEARCH

    override fun onBackPress(): Boolean {
        hudViewModel.exitSearch()
        return false
    }

    override fun subscribeToViewEvents() {
        hudViewModel.selectSubscribe(
            HudState::searchQuery,
            deliveryMode = UniqueOnly("query")
        ) {
            if (it != null) {
                if (it.lowercase().contains("stickerbr")) {
                    onStickerBrEntered(it)
                } else {
                    onSearchQueryEntered(it)
                }
            } else {
                viewModel.onQueryClear()
            }
        }

        viewModel.selectSubscribe(SearchState::clickedSong) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                onSongClicked(clickedId)
                viewModel.clearClickedSong()
            }
        }

        viewModel.selectSubscribe(SearchState::clickedGame) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                onGameClicked(clickedId)
                viewModel.clearClickedGame()
            }
        }

        viewModel.selectSubscribe(SearchState::clickedComposer) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                onComposerClicked(clickedId)
                viewModel.clearClickedComposer()
            }
        }
    }

    private fun onSearchQueryEntered(query: String) {
        tracker.logSearch(query)
        viewModel.startQuery(query)
    }

    private fun onStickerBrEntered(query: String) {
        tracker.logStickerBr()
        viewModel.showStickerBr(query)
    }

    private fun onGameClicked(id: Long) = withState(viewModel) { state ->
        val game = state.data.games()?.firstOrNull { it.id == id }

        if (game == null) {
            showError("Failed to show game.")
            return@withState
        }

        tracker.logSearchSuccess(
            state.data.query ?: "",
            TrackingScreen.DETAIL_GAME,
            id.toString()
        )

        getFragmentRouter().showSongListForGame(id, game.name)
        hudViewModel.hideSearch()
    }

    private fun onSongClicked(id: Long) = withState(viewModel, hudViewModel) { state, hudState ->
        val song = state.data.songs()?.firstOrNull { it.id == id }

        if (song == null) {
            showError("Failed to show song.")
            return@withState
        }

        tracker.logSearchSuccess(
            state.data.query ?: "",
            TrackingScreen.SHEET_VIEWER,
            id.toString()
        )

        val transposition = hudState.selectedPart.apiId

        getFragmentRouter().showSongViewer(
            id,
            song.name,
            song.gameName,
            transposition
        )

        hudViewModel.hideSearch()
    }

    private fun onComposerClicked(id: Long) = withState(viewModel) { state ->
        val composer = state.data.composers()?.firstOrNull { it.id == id }

        if (composer == null) {
            showError("Failed to show composer.")
            return@withState
        }

        tracker.logSearchSuccess(
            state.data.query ?: "",
            TrackingScreen.DETAIL_COMPOSER,
            id.toString()
        )

        getFragmentRouter().showSongListForComposer(id, composer.name)
        hudViewModel.hideSearch()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
