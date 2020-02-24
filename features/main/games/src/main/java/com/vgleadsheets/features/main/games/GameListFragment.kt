package com.vgleadsheets.features.main.games

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_games.list_games
import javax.inject.Inject

@Suppress("TooManyFunctions")
class GameListFragment : VglsFragment() {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: GameListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    private var apiAvailableErrorShown = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_games.adapter = adapter
        list_games.layoutManager = LinearLayoutManager(context)
        list_games.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )

        hudViewModel.dontAlwaysShowBack()

        hudViewModel.selectSubscribe(HudState::digest) {
            viewModel.onDigestUpdate(it)
        }

        hudViewModel.selectSubscribe(HudState::updateTime) {
            viewModel.onTimeUpdate(it)
        }

        hudViewModel.selectSubscribe(HudState::parts) { parts ->
            viewModel.onSelectedPartUpdate(parts.firstOrNull { it.selected } )
        }

        viewModel.selectSubscribe(GameListState::clickedGbListModel, deliveryMode = UniqueOnly("clicked")) {
            val clickedGameId = it?.dataId
            if (clickedGameId != null) {
                showSongList(clickedGameId)
            }
        }

        viewModel.selectSubscribe(GameListState::gbApiNotAvailable, deliveryMode = UniqueOnly("clicked")) {
            if (it == true) {
                if (!apiAvailableErrorShown) {
                    apiAvailableErrorShown = true
                    showError(getString(R.string.error_no_gb_api))
                }
            }
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        adapter.submitList(state.listModels)
    }

    private fun showSongList(clickedGameId: Long) {
        getFragmentRouter().showSongListForGame(clickedGameId)
    }

    override fun getLayoutId() = R.layout.fragment_games

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        const val LOADING_ITEMS = 15

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6

        fun newInstance() = GameListFragment()
    }
}
