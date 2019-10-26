package com.vgleadsheets.features.main.viewer

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.args
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.components.SheetListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import kotlinx.android.synthetic.main.fragment_viewer.list_sheets
import kotlinx.android.synthetic.main.fragment_viewer.pager_sheets
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ViewerFragment : VglsFragment(), SheetListModel.ImageListener {
    @Inject
    lateinit var viewerViewModelFactory: ViewerViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: ViewerViewModel by fragmentViewModel()

    private val songArgs: SongArgs by args()

    private val adapter = ComponentAdapter()

    override fun onClicked() = withState(hudViewModel) { state ->
        if (state.hudVisible) {
            hudViewModel.hideHud()
        } else {
            hudViewModel.showHud()
        }
    }

    override fun onLoadFailed(imageUrl: String, ex: Exception?) {
        showError("Image load failed: ${ex?.message ?: "Unknown Error"}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager_sheets?.adapter = adapter

        list_sheets?.adapter = adapter
        list_sheets?.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        hudViewModel.showHud()
        hudViewModel.stopHudTimer()
        hudViewModel.resetAvailableParts()
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, viewerState ->
        hudViewModel.alwaysShowBack()

        if (hudState.hudVisible && !hudState.searchVisible) {
            hudViewModel.startHudTimer()
        }

        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        when (viewerState.song) {
            is Fail -> showError(
                viewerState.song.error.message
                    ?: viewerState.song.error::class.simpleName ?: "Unknown Error"
            )
            is Success -> showSheet(viewerState.song(), selectedPart)
        }
    }

    override fun onBackPress() = withState(hudViewModel) { hudState ->
        if (hudState.hudVisible) {
            return@withState false
        } else {
            hudViewModel.showHud()
            return@withState true
        }
    }

    override fun getLayoutId() = R.layout.fragment_viewer

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${songArgs.songId}"

    private fun showSheet(sheet: Song?, partSelection: PartSelectorItem) {
        if (sheet == null) {
            showEmptyState()
            return
        }

        val parts = sheet.parts
        if (parts != null) {
            hudViewModel.setAvailableParts(parts)
        } else {
            showError("Unable to determine which parts are available for this sheet: $partSelection.")
        }

        val selectedPart = sheet.parts?.firstOrNull { it.name == partSelection.apiId }

        if (selectedPart == null) {
            showError("This sheet doesn't include the part you selected. Choose another.")
            return
        }

        val listComponents = selectedPart.pages?.map {
            SheetListModel(
                it.imageUrl,
                this
            )
        }

        if (adapter.currentList != listComponents) {
            adapter.submitList(listComponents)
            Timber.w("Lists changed, submitting.")
        } else {
            Timber.i("Lists equivalent, not submitting.")
        }
    }

    private fun showEmptyState() {
        showError("No sheet found.")
    }

    companion object {
        fun newInstance(sheetArgs: SongArgs): ViewerFragment {
            val fragment = ViewerFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, sheetArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
