package com.vgleadsheets.features.main.sheet

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.getYoutubeSearchUrlForQuery
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class SheetDetailFragment : AsyncListFragment<SheetDetailData, SheetDetailState>() {
    @Inject
    lateinit var sheetViewModelFactory: SheetDetailViewModel.Factory

    override val loadStatusProperty = SheetDetailState::loadStatus

    override val viewModel: SheetDetailViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun getTrackingScreen() = TrackingScreen.DETAIL_SHEET

    @SuppressWarnings("ComplexMethod")
    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(SheetDetailState::clickedCtaModel) {
            when (it?.dataId?.toInt()) {
                R.drawable.ic_description_24dp -> showSongViewer()
                R.drawable.ic_play_circle_filled_24 -> showYoutubeSearch()
                null -> Unit
                else -> TODO("Unimplemented button")
            }

            viewModel.clearClicked()
        }

        viewModel.selectSubscribe(SheetDetailState::clickedComposerModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                if (clickedId != SheetDetailViewModel.ID_COMPOSER_MULTIPLE) {
                    getFragmentRouter().showSongListForComposer(clickedId, it.value)
                } else {
                    showError("Links to multiple composers coming soon!")
                }
            }

            viewModel.clearClicked()
        }

        viewModel.selectSubscribe(SheetDetailState::clickedGameModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                getFragmentRouter().showSongListForGame(clickedId, it.value)
            }

            viewModel.clearClicked()
        }

        viewModel.selectSubscribe(SheetDetailState::clickedTagValueModel) {
            handleTagClick(it)
        }

        viewModel.selectSubscribe(SheetDetailState::clickedRatingStarModel) {
            handleTagClick(it)
        }

        viewModel.selectSubscribe(SheetDetailState::data) {
            if (it.song is Success) {
                val parts = it.song()?.parts

                if (parts != null) {
                    hudViewModel.setAvailableParts(parts)
                } else {
                    showError("Unable to determine which parts are available for this sheet.")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        hudViewModel.resetAvailableParts()
    }

    private fun showSongViewer() = withState(viewModel, hudViewModel) { state, hudState ->
        val song = state.data.song()

        val transposition = hudState
            .parts
            .firstOrNull { it.selected }
            ?.apiId ?: "Error"

        if (song != null) {
            getFragmentRouter().showSongViewer(
                song.id,
                song.name,
                song.gameName,
                transposition
            )
        } else {
            showError("Cannot find this sheet.")
        }
    }

    private fun showYoutubeSearch() = withState(viewModel) {
        if (it.data.song is Success) {
            val song = it.data.song()!!
            val query = "${song.gameName} - ${song.name}"

            val youtubeUrl = getYoutubeSearchUrlForQuery(query)

            getFragmentRouter().goToWebUrl(youtubeUrl)
        }
    }

    private fun handleTagClick(it: ListModel?) {
        when (val clickedId = it?.dataId) {
            null -> Unit
            else -> showTagValue(clickedId)
        }

        viewModel.clearClicked()
    }

    private fun showTagValue(clickedId: Long) {
        getFragmentRouter().showSongListForTagValue(clickedId)
    }

    companion object {
        fun newInstance(idArgs: IdArgs): SheetDetailFragment {
            val fragment = SheetDetailFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
