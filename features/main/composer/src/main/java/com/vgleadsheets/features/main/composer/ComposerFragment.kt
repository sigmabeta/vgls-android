package com.vgleadsheets.features.main.composer

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
import com.vgleadsheets.components.GiantBombTitleListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_composer.list_songs
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ComposerFragment : VglsFragment(),
    GiantBombTitleListModel.EventHandler,
    ImageNameCaptionListModel.EventHandler {
    @Inject
    lateinit var composerViewModelFactory: ComposerViewModel.Factory

    private val viewModel: ComposerViewModel by fragmentViewModel()

    private val hudViewModel: HudViewModel by existingViewModel()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: ImageNameCaptionListModel) {
        showSongViewer(clicked.dataId)
    }

    override fun onGbModelNotChecked(vglsId: Long, name: String) {
        viewModel.onGbComposerNotChecked(vglsId, name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_songs.adapter = adapter
        list_songs.layoutManager = LinearLayoutManager(context)
        list_songs.setInsetListenerForPadding(topOffset = topOffset, bottomOffset = bottomOffset)
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, composerState ->
        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        val composer = composerState.composer
        val songs = composerState.songs

        val listModels = constructList(composer, songs, selectedPart)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_composer

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        composer: Async<Composer>,
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        if (composer !is Success) return emptyList()

        val songListModels = createContentListModels(songs, selectedPart)

        // Pass in `songs` so we know whether to show a sheet counter or not.
        val titleListModel =
            arrayListOf(createTitleListModel(composer(), songs, songListModels.size))

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

    private fun createTitleListModel(
        composer: Composer,
        songs: Async<List<Song>>,
        songCount: Int
    ) = GiantBombTitleListModel(
        composer.id,
        composer.giantBombId,
        composer.name,
        generateSheetCountText(songs, songCount),
        composer.photoUrl,
        R.drawable.placeholder_composer,
        this
    )

    private fun generateSheetCaption(song: Song): String {
        return when (song.composers?.size) {
            1 -> song.composers?.firstOrNull()?.name ?: "Unknown Composer"
            else -> "Various Composers"
        }
    }

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
                    generateSheetCaption(it),
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

    private fun showSongViewer(clickedSongId: Long) {
        getFragmentRouter().showSongViewer(clickedSongId)
    }

    companion object {
        const val LOADING_ITEMS = 15

        fun newInstance(idArgs: IdArgs): ComposerFragment {
            val fragment = ComposerFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
