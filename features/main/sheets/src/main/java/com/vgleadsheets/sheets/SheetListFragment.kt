package com.vgleadsheets.sheets

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.vgleadsheets.IdArgs
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ListView
import com.vgleadsheets.repository.*
import kotlinx.android.synthetic.main.fragment_sheet.*
import timber.log.Timber
import javax.inject.Inject

class SheetListFragment : VglsFragment(), ListView {
    @Inject
    lateinit var sheetListViewModelFactory: SheetListViewModel.Factory

    private val viewModel: SheetListViewModel by fragmentViewModel()

    private val adapter = SheetListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list_sheets.adapter = adapter
        list_sheets.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClick(position: Int) {
        viewModel.onItemClick(position)
    }

    override fun invalidate() = withState(viewModel) { state ->
        when (state.data) {
            is Fail -> showError(state.data.error.message ?: state.data.error::class.simpleName ?: "Unknown Error")
            is Success -> showData(state.data())
        }
    }

    override fun getLayoutId() = R.layout.fragment_sheet

    private fun showData(data: Data<List<Song>>?) {
        when (data) {
            is Empty -> showLoading()
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Network -> hideLoading()
            is Storage -> showSheets(data())
        }
    }

    private fun showSheets(songs: List<Song>) {
        adapter.dataset = songs
    }

    private fun showLoading() {
        Timber.i("Loading...")
        progress_loading.fadeInFromZero()
        list_sheets.fadeOutPartially()
    }

    private fun hideLoading() {
        list_sheets.fadeIn()
        progress_loading.fadeOutGone()
    }


    companion object {
        fun newInstance(idArgs: IdArgs): SheetListFragment {
            val fragment = SheetListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}