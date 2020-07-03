package com.vgleadsheets.features.main.sheet

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
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
            val clickedId = it?.dataId?.toInt()

            when (clickedId) {
                R.drawable.ic_description_24dp -> showSongViewer()
                R.drawable.ic_favorite_disabled_24 -> showError("Coming soon!")
                R.drawable.ic_download_24 -> showError("Coming soon!")
                null -> Unit
                else -> TODO("Unimplemented button")
            }

            viewModel.clearClicked()
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