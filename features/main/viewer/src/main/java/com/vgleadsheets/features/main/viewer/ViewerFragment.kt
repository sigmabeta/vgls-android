package com.vgleadsheets.features.main.viewer

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.squareup.picasso.Callback
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.loadImageFull
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image_sheet.setOnClickListener { hudViewModel.showHud() }
    }

    override fun onDestroy() {
        super.onDestroy()
        hudViewModel.showHud()
        hudViewModel.resetAvailableParts()
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, viewerState ->
        if (hudState.hudVisible) {
            hudViewModel.startHudTimer()
        }

        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        when (viewerState.song) {
            is Fail -> showError(viewerState.song.error.message
                ?: viewerState.song.error::class.simpleName ?: "Unknown Error")
            is Success -> showData(viewerState.song(), selectedPart)
        }
    }

    override fun onBackPress() {
        hudViewModel.showHud()
    }

    override fun getLayoutId() = R.layout.fragment_viewer

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${songArgs.songId}"

    private fun showData(data: Data<Song>?, selectedPart: PartSelectorItem) {
        when (data) {
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Storage -> showSheet(data(), selectedPart)
        }
    }

    private fun showSheet(sheet: Song, selectedPart: PartSelectorItem) {
        val parts = sheet.parts
        if (parts != null) {
            hudViewModel.setAvailableParts(parts)
        } else {
            showError("Unable to determine which parts are available for this sheet.")
        }

        showLoading()
        image_sheet.loadImageFull(
            "https://vgleadsheets.com" +
                    "/assets/sheets/png/${selectedPart.apiId}/${Uri.encode(sheet.filename)}-1.png",
            object : Callback {
                override fun onSuccess() {
                    hideLoading()
                }

                override fun onError() {
                    hideLoading()
                    showError("Unable to load this sheet. Trying again for the C part...")
                }
            })
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        image_sheet.fadeOutPartially()
    }

    private fun hideLoading() {
        image_sheet.fadeIn()
        progress_loading.fadeOutGone()
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
