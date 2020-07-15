package com.vgleadsheets.features.main.game

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class GameFragment : AsyncListFragment<GameData, GameState>() {
    @Inject
    lateinit var gameViewModelFactory: GameViewModel.Factory

    override val viewModel: GameViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun getTrackingScreen() = TrackingScreen.DETAIL_GAME

    override fun getDetails() = (idArgs.id - VglsApiGame.ID_OFFSET).toString()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(GameState::clickedListModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showSongViewer(clickedId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongViewer(clickedSongId: Long) =
        withState(viewModel, hudViewModel) { state, hudState ->
            val song = state.data.songs()?.first { it.id == clickedSongId }

            if (song == null) {
                showError("Failed to show song.")
                return@withState
            }

            val transposition = hudState
                .parts
                .firstOrNull { it.selected }
                ?.apiId ?: "Error"

            getFragmentRouter().showSongViewer(
                clickedSongId,
                song.name,
                song.gameName,
                transposition
            )
        }

    companion object {
        fun newInstance(idArgs: IdArgs): GameFragment {
            val fragment = GameFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
