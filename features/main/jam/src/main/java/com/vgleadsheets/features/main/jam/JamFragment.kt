package com.vgleadsheets.features.main.jam

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.model.song.Song
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class JamFragment : AsyncListFragment<JamData, JamState>() {
    @Inject
    lateinit var jamViewModelFactory: JamViewModel.Factory

    override val viewModel: JamViewModel by fragmentViewModel()

    private val idArgs: IdArgs by args()

    private var refreshLauncher: Disposable? = null

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(JamState::clickedCurrentSongModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showCurrentSong()
                viewModel.clearClicked()
            }
        }

        viewModel.selectSubscribe(JamState::clickedHistoryModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showHistorySong(clickedId)
                viewModel.clearClicked()
            }
        }

        viewModel.selectSubscribe(JamState::clickedSetListModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showSetlistSong(clickedId)
                viewModel.clearClicked()
            }
        }

        viewModel.selectSubscribe(JamState::clickedCtaModel) {
            val clickedId = it?.dataId

            when (clickedId?.toInt()) {
                R.drawable.ic_playlist_play_black_24dp -> followJam()
                R.drawable.ic_refresh_24dp -> viewModel.refreshJam()
                R.drawable.ic_delete_black_24dp -> viewModel.deleteJam()
                null -> { }
                else -> TODO("Unimplemented button")
            }
        }

        viewModel.selectSubscribe(JamState::refreshError) {
            if (it != null) {
                showError(it)
                viewModel.clearClicked()
            }
        }

        // Quit the screen when the jam is deleted.
        viewModel.asyncSubscribe(
            JamState::deletion,
            deliveryMode = uniqueOnly("deletion")
        ) {
            activity?.onBackPressed()
        }

        // Fire up one refresh whenever we launch the screen. // TODO Not this
        refreshLauncher = viewModel.selectSubscribe(
            JamState::listModels,
            deliveryMode = uniqueOnly("jam")
        ) { listModels ->
            val titleListModel = listModels
                .firstOrNull { it is TitleListModel }

            if (titleListModel != null) {
                viewModel.refreshJam()
                refreshLauncher?.dispose()
            }
        }
    }

    private fun showCurrentSong() = withState(viewModel) { state ->
        val currentSong = state.data.jam()?.currentSong
        if (currentSong == null) {
            showError("Failed to show song.")
            return@withState
        }
        showSongViewer(currentSong)
    }

    private fun showHistorySong(clickedId: Long) = withState(viewModel) { state ->
        val historySong = state
            .data
            .jam()
            ?.songHistory
            ?.first { it.id == clickedId }
            ?.song

        if (historySong == null) {
            showError("Failed to show song.")
            return@withState
        }

        showSongViewer(historySong)
    }

    private fun showSetlistSong(clickedId: Long) = withState(viewModel) { state ->
        val historySong = state
            .data
            .setlist()
            ?.first { it.id == clickedId }
            ?.song

        if (historySong == null) {
            showError("Failed to show song.")
            return@withState
        }

        showSongViewer(historySong)
    }

    private fun showSongViewer(song: Song) =
        withState(hudViewModel) { hudState ->
            tracker.logSongView(
                song.name,
                song.gameName,
                hudState.parts.first { it.selected }.apiId,
                null
            )
            getFragmentRouter().showSongViewer(song.id)
        }

    private fun followJam() = withState(viewModel) {
        getFragmentRouter().showJamViewer(it.jamId)
    }

    companion object {
        fun newInstance(idArgs: IdArgs): JamFragment {
            val fragment = JamFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}