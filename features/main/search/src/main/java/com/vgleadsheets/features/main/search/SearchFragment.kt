package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.tracking.TrackingScreen
import java.util.Locale
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SearchFragment : AsyncListFragment<SearchData, SearchState>() {
    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory

    override val viewModel: SearchViewModel by fragmentViewModel()

    override fun getTrackingScreen() = TrackingScreen.SEARCH

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
                if (it.toLowerCase(Locale.getDefault()).contains("stickerbr")) {
                    onStickerBrEntered()
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

    private fun onStickerBrEntered() {
        tracker.logStickerBr()
        viewModel.showStickerBr()
    }

    private fun onGameClicked(id: Long) = withState(viewModel) { state ->
        val game = state.data.games()?.firstOrNull { it.id == id }

        if (game == null) {
            showError("Failed to show game.")
            return@withState
        }

        tracker.logSearchSuccess(
            state.data.query ?: "",
            TrackingScreen.GAME_DETAIL,
            id.toString()
        )

        hudViewModel.exitSearch()
        getFragmentRouter().showSongListForGame(id)
    }

    private fun onSongClicked(id: Long) = withState(viewModel) { state ->
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

        hudViewModel.exitSearch()
        getFragmentRouter().showSongViewer(id)
    }

    private fun onComposerClicked(id: Long) = withState(viewModel) { state ->
        val composer = state.data.composers()?.firstOrNull { it.id == id }

        if (composer == null) {
            showError("Failed to show composer.")
            return@withState
        }

        tracker.logSearchSuccess(
            state.data.query ?: "",
            TrackingScreen.COMPOSER_DETAIL,
            id.toString()
        )

        hudViewModel.exitSearch()
        getFragmentRouter().showSongListForComposer(id)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
