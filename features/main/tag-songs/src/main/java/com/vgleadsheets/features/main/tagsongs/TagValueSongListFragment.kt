package com.vgleadsheets.features.main.tagsongs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_tag_value_song_list.list_tag_value_songs
import javax.inject.Inject

@Suppress("TooManyFunctions")
class TagValueSongListFragment : VglsFragment(),
    ImageNameCaptionListModel.EventHandler {

    @Inject
    lateinit var tagValueViewModelFactory: TagValueSongListViewModel.Factory

    private val viewModel: TagValueSongListViewModel by fragmentViewModel()

    private val hudViewModel: HudViewModel by existingViewModel()

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

        list_tag_value_songs.adapter = adapter
        list_tag_value_songs.layoutManager = LinearLayoutManager(context)
        list_tag_value_songs.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, state ->
        hudViewModel.alwaysShowBack()

        val selectedPart = hudState.parts.first { it.selected }

        val listModels = constructList(state.tagValue, state.songs, selectedPart)
        adapter.submitList(listModels)
    }

    private fun constructList(
        tagValue: Async<TagValue>,
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        if (tagValue !is Success) return emptyList()

        val songListModels = createContentListModels(songs, selectedPart)

        // Pass in `songs` so we know whether to show a sheet counter or not.
        val titleListModel =
            arrayListOf(createTitleListModel(tagValue(), songs, songListModels.size))

        return titleListModel + songListModels
    }

    private fun createTitleListModel(
        tagValue: TagValue,
        songs: Async<List<Song>>,
        songCount: Int
    ) = TitleListModel(
        R.string.title.toLong(),
        getString(R.string.title_tag_value_songs, tagValue.tagKeyName, tagValue.name),
        generateSheetCountText(songs, songCount)
    )

    private fun generateSheetCountText(
        songs: Async<List<Song>>,
        songCount: Int
    ) = if (songs is Success) getString(R.string.subtitle_sheets_count, songCount) else ""

    private fun createContentListModels(
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ) = when (songs) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(songs.error)
        is Success -> createSuccessListModels(songs(), selectedPart)
    }

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
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

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

    private fun showSongViewer(songId: Long) {
        getFragmentRouter().showSongViewer(songId)
    }

    override fun getLayoutId() = R.layout.fragment_tag_value_song_list

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    companion object {
        const val LOADING_ITEMS = 15
        fun newInstance(idArgs: IdArgs): TagValueSongListFragment {
            val fragment = TagValueSongListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
