package com.vgleadsheets.songs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ListView
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Empty
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Network
import com.vgleadsheets.repository.Storage
import com.vgleadsheets.setSimpleInsetListener
import kotlinx.android.synthetic.main.fragment_sheet.*
import javax.inject.Inject

class SongListFragment : VglsFragment(), ListView {
    @Inject
    lateinit var sheetListViewModelFactory: SongListViewModel.Factory

    private val viewModel: SongListViewModel by fragmentViewModel()

    private val adapter = SongListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_sheets.adapter = adapter
        list_sheets.layoutManager = LinearLayoutManager(context)
        list_sheets.setSimpleInsetListener()
    }

    override fun onItemClick(position: Int) {
        viewModel.onItemClick(position)
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (state.clickedSongId != null) {
            showSongViewer(state.clickedSongId)
            viewModel.onSongViewerLaunch()
            return@withState
        }

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
            is Storage -> showSongs(data())
        }
    }

    private fun showSongs(songs: List<Song>) {
        adapter.dataset = songs
    }

    private fun showSongViewer(clickedSongId: Long) {
        (activity as FragmentRouter).showSongViewer(clickedSongId)
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        list_sheets.fadeOutPartially()
    }

    private fun hideLoading() {
        list_sheets.fadeIn()
        progress_loading.fadeOutGone()
    }

    companion object {
        fun newInstance(idArgs: IdArgs): SongListFragment {
            val fragment = SongListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
