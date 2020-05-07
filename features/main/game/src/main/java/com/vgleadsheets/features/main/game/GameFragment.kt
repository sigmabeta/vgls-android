package com.vgleadsheets.features.main.game

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import javax.inject.Inject

@Suppress("TooManyFunctions")
class GameFragment : AsyncListFragment<GameData, GameState>() {
    @Inject
    lateinit var gameViewModelFactory: GameViewModel.Factory

    override val viewModel: GameViewModel by fragmentViewModel()

    private val idArgs: IdArgs by args()

    private var apiAvailableErrorShown = false

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(GameState::clickedListModel, deliveryMode = UniqueOnly("clicked")) {
            val clickedId = it?.dataId
            if (clickedId != null) {
                showSongViewer(clickedId)
            }
        }

        viewModel.selectSubscribe(GameState::gbApiNotAvailable, deliveryMode = UniqueOnly("api")) {
            if (it == true) {
                if (!apiAvailableErrorShown) {
                    apiAvailableErrorShown = true
                    showError(getString(R.string.error_no_gb_api))
                }
            }
        }
    }

    private fun showSongViewer(clickedSongId: Long) =
        withState(hudViewModel, viewModel) { hudState, state ->
            val song = state.data.songs()?.first { it.id == clickedSongId }

            if (song == null) {
                showError("Failed to show song.")
                return@withState
            }
            tracker.logSongView(
                song.name,
                song.gameName,
                hudState.parts.first { it.selected }.apiId,
                null
            )
            getFragmentRouter().showSongViewer(clickedSongId)
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
