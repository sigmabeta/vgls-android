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
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.loadImageFull
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
        image_sheet.setOnClickListener { hudViewModel.showHud()}
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, viewerState ->
        if (hudState.hudVisible) {
            hudViewModel.startHudTimer()
        }

        when (viewerState.data) {
            is Fail -> showError(viewerState.data.error.message ?: viewerState.data.error::class.simpleName ?: "Unknown Error")
            is Success -> showData(viewerState.data())
        }
    }

    override fun onBackPress() {
        hudViewModel.showHud()
    }

    override fun getLayoutId() = R.layout.fragment_viewer

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${songArgs.songId}"

    private fun showData(data: Data<String>?) {
        when (data) {
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Storage -> showSheet(data())
        }
    }

    private fun showSheet(sheet: String) {
//        hudViewModel.hideHud()
        image_sheet.loadImageFull(
            "https://vgleadsheets.com/assets/sheets/png/C/" + Uri.encode(sheet) + "-1.png")
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
