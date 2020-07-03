package com.vgleadsheets.features.main.composer

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ComposerFragment : AsyncListFragment<ComposerData, ComposerState>() {
    @Inject
    lateinit var composerViewModelFactory: ComposerViewModel.Factory

    override val viewModel: ComposerViewModel by fragmentViewModel()

    private val idArgs: IdArgs by args()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(ComposerState::clickedListModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showSongViewer(clickedId)
                viewModel.clearClicked()
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
        fun newInstance(idArgs: IdArgs): ComposerFragment {
            val fragment = ComposerFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
