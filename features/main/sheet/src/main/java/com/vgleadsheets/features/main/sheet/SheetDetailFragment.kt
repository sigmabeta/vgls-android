package com.vgleadsheets.features.main.sheet

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.getYoutubeSearchUrlForQuery
import javax.inject.Inject

class SheetDetailFragment : AsyncListFragment<SheetDetailData, SheetDetailState>() {
    @Inject
    lateinit var sheetViewModelFactory: SheetDetailViewModel.Factory

    override val viewModel: SheetDetailViewModel by fragmentViewModel()

    private val idArgs: IdArgs by args()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

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

        viewModel.selectSubscribe(SheetDetailState::clickedDetailModel) {
            when (val clickedId = it?.dataId) {
                R.string.label_detail_pages.toLong() -> Unit
                null -> Unit
                else -> showComposer(clickedId)
            }

            viewModel.clearClicked()
        }

        viewModel.selectSubscribe(SheetDetailState::clickedTagValueModel) {
            handleTagClick(it)
        }

        viewModel.selectSubscribe(SheetDetailState::clickedRatingStarModel) {
            handleTagClick(it)
        }
    }

    private fun showSongViewer() = withState(viewModel, hudViewModel) { state, hudState ->
        val song = state.data.song()

        if (song != null) {
            tracker.logSongView(
                song.name,
                song.gameName,
                hudState.parts.first { it.selected }.apiId,
                null
            )
            getFragmentRouter().showSongViewer(song.id)
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

    private fun showComposer(clickedId: Long) {
        if (clickedId != SheetDetailViewModel.ID_COMPOSER_MULTIPLE) {
            getFragmentRouter().showSongListForComposer(clickedId)
        } else {
            showError("Links to multiple composers coming soon!")
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