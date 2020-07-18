package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class GameListFragment : ListFragment<Game, GameListState>() {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    override val loadStatusProperty = GameListState::loadStatus

    override fun getTrackingScreen() = TrackingScreen.LIST_GAME

    override val viewModel: GameListViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(GameListState::clickedListModel) {
            val clickedGameId = it?.dataId

            if (clickedGameId != null) {
                getFragmentRouter().showSongListForGame(
                    clickedGameId,
                    it.name
                )

                viewModel.clearClicked()
            }
        }
    }

    companion object {
        fun newInstance() = GameListFragment()
    }
}
