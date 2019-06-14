package com.vgleadsheets.viewer

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.*
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.games.R
import com.vgleadsheets.loadImageHighQuality
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Storage
import kotlinx.android.synthetic.main.fragment_viewer.*
import javax.inject.Inject

class ViewerFragment : VglsFragment() {
    @Inject
    lateinit var ViewerViewModelFactory: ViewerViewModel.Factory

    private val viewModel: ViewerViewModel by fragmentViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun getLayoutId() = R.layout.fragment_viewer

    override fun invalidate() = withState(viewModel) { state ->
        when (state.data) {
            is Fail -> showError(state.data.error.message ?: state.data.error::class.simpleName ?: "Unknown Error")
            is Success -> showData(state.data())
        }
    }

    private fun showData(data: Data<String>?) {
        when (data) {
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Storage -> showSheet(data())
        }
    }

    private fun showSheet(sheet: String) {
        image_sheet.loadImageHighQuality("https://vgleadsheets.com/assets/sheets/png/C/" + Uri.encode(sheet) + "-1.png", false, null)
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
