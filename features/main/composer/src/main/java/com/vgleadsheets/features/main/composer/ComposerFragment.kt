package com.vgleadsheets.features.main.composer

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ComposerFragment : AsyncListFragment<ComposerData, ComposerState>() {
    @Inject
    lateinit var composerViewModelFactory: ComposerViewModel.Factory

    override val viewModel: ComposerViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun getTrackingScreen() = TrackingScreen.COMPOSER_DETAIL

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
        fun newInstance(idArgs: IdArgs): ComposerFragment {
            val fragment = ComposerFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
