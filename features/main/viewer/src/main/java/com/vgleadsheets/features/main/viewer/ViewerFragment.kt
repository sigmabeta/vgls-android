package com.vgleadsheets.features.main.viewer

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.viewer.pages.PageAdapter
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Storage
import kotlinx.android.synthetic.main.fragment_viewer.*
import javax.inject.Inject

class ViewerFragment : VglsFragment() {
    @Inject
    lateinit var viewerViewModelFactory: ViewerViewModel.Factory

    private val hudViewModel: HudViewModel by activityViewModel()

    private val viewModel: ViewerViewModel by fragmentViewModel()

    private val songArgs: SongArgs by args()

    private val adapter = PageAdapter(this)

    fun onPageLoadError(pageNumber: Int) {
        showError("Unable to load page $pageNumber")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager_sheets?.adapter = adapter
        pager_sheets?.setOnClickListener { hudViewModel.showHud() }

        list_sheets?.adapter = adapter
        list_sheets?.setOnClickListener { hudViewModel.showHud() }
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
            is Success -> showData(viewerState.song(), selectedPart)
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

    private fun showData(data: Data<Song>?, selectedPart: PartSelectorItem) {
        when (data) {
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Storage -> showSheet(data(), selectedPart)
        }
    }

    private fun showSheet(sheet: Song, partSelection: PartSelectorItem) {
        val parts = sheet.parts
        if (parts != null) {
            hudViewModel.setAvailableParts(parts)
        } else {
            showError("Unable to determine which parts are available for this sheet: $partSelection.")
        }

        val selectedPart = sheet.parts?.first { it.name == partSelection.apiId }
        adapter.dataset = selectedPart?.pages
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
