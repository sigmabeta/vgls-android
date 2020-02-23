package com.vgleadsheets.features.main.songs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_song.list_sheets
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SongListFragment : VglsFragment(), ImageNameCaptionListModel.EventHandler {
    @Inject
    lateinit var sheetListViewModelFactory: SongListViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: SongListViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: ImageNameCaptionListModel) {
        showSongViewer(clicked.dataId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_sheets.adapter = adapter
        list_sheets.layoutManager = LinearLayoutManager(context)
        list_sheets.setInsetListenerForPadding(topOffset = topOffset, bottomOffset = bottomOffset)
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, songListState ->
        hudViewModel.dontAlwaysShowBack()
        val selectedPart = hudState.parts.first { it.selected }

        val songs = songListState.songs
        if (songs is Fail) {
            showError(songs.error)
        }

        val listModels = constructList(songs, selectedPart)
        adapter.submitList(listModels)
    }

    private fun constructList(
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val titleListModel = arrayListOf(createTitleListModel())
        val songListModels = createContentListModels(songs, selectedPart)

        return titleListModel + songListModels
    }

    private fun createContentListModels(
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ) = when (songs) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(songs.error)
        is Success -> createSuccessListModels(songs(), selectedPart)
    }

    private fun createTitleListModel() = TitleListModel(
        R.string.subtitle_all_sheets.toLong(),
        getString(R.string.app_name),
        getString(R.string.subtitle_all_sheets)
    )

    private fun generateSheetCountText(
        songs: Async<List<Song>>,
        songCount: Int
    ) = if (songs is Success) getString(R.string.subtitle_sheets_count, songCount) else ""

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            listModels.add(
                LoadingNameCaptionListModel(index)
            )
        }

        return listModels
    }

    private fun createErrorStateListModel(error: Throwable) =
        arrayListOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createSuccessListModels(
        songs: List<Song>,
        selectedPart: PartSelectorItem
    ) = if (songs.isEmpty()) {
        arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No songs found at all. Check your internet connection?"
            )
        )
    } else {
        val availableSongs = filterSongs(songs, selectedPart)

        if (availableSongs.isEmpty()) {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No songs found with a ${selectedPart.apiId} part. Try another part?"
                )
            )
        } else {
            availableSongs.map {
                val thumbUrl = it
                    .parts
                    ?.first { part -> part.name == selectedPart.apiId }
                    ?.pages
                    ?.first()
                    ?.imageUrl

                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    it.gameName,
                    thumbUrl,
                    R.drawable.placeholder_sheet,
                    this
                )
            }
        }
    }

    private fun filterSongs(
        songs: List<Song>,
        selectedPart: PartSelectorItem
    ) = songs.filter { song ->
        song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
    }

    override fun getLayoutId() = R.layout.fragment_song

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun showSongViewer(clickedSongId: Long) =
        withState(hudViewModel, viewModel) { hudState, state ->
            val song = state.songs()?.first { it.id == clickedSongId }

            if (song == null) {
                showError("Failed to show song.")
                return@withState
            }
            tracker.logSongView(
                song.name,
                song.gameName,
                hudState.parts.first { it.selected }.apiId,
                null
            )
            getFragmentRouter().showSongViewer(clickedSongId)
        }

    companion object {
        const val LOADING_ITEMS = 15
        fun newInstance() = SongListFragment()
    }
}
