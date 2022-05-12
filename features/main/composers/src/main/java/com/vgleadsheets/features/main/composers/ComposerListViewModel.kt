package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

class ComposerListViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerListState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : ListViewModel<Composer, ComposerListState>(initialState),
    ImageNameCaptionListModel.EventHandler {
    init {
        fetchComposers()
    }

    override fun onClicked(clicked: ImageNameCaptionListModel) = setState {
        copy(
            clickedListModel = clicked
        )
    }

    override fun clearClicked() = setState {
        copy(
            clickedListModel = null
        )
    }

    override fun createTitleListModel(): TitleListModel {
        val spec = PerfSpec.COMPOSERS

        perfTracker.onTitleLoaded(spec)
        perfTracker.onTransitionStarted(spec)

        return TitleListModel(
            resourceProvider.getString(R.string.app_name),
            resourceProvider.getString(R.string.subtitle_composer),
        )
    }

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No composers found at all. Check your internet connection?",
    )

    override fun createSuccessListModels(
        data: List<Composer>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part
    ): List<ListModel> {
        val availableComposers = filterComposers(data, selectedPart)

        return if (availableComposers.isEmpty()) arrayListOf(
            EmptyStateListModel(
                R.drawable.ic_album_24dp,
                "No composers found with a ${selectedPart.apiId} part. Try another part?",
            )
        ) else {
            val spec = PerfSpec.COMPOSERS

            perfTracker.onPartialContentLoad(spec)
            perfTracker.onFullContentLoad(spec)

            availableComposers.map {
                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    generateSubtitleText(it.songs),
                    it.photoUrl,
                    R.drawable.placeholder_composer,
                    this,
                )
            }
        }
    }

    private fun fetchComposers() {
        repository.getComposers()
            .execute { data ->
                copy(
                    data = data,
                    listModels = constructList(
                        data,
                        updateTime,
                        digest,
                        selectedPart
                    )
                )
            }
    }

    @Suppress("LoopWithTooManyJumpStatements")
    private fun generateSubtitleText(items: List<Song>?): String {
        if (items.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = items.size

        while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                break
            }

            if (numberOfOthers == 0) {
                break
            }

            if (index != 0) {
                builder.append(resourceProvider.getString(R.string.subtitle_separator))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(
                resourceProvider.getString(
                    R.string.subtitle_suffix_others,
                    numberOfOthers
                )
            )
        }

        return builder.toString()
    }

    private fun filterComposers(
        composers: List<Composer>,
        selectedPart: Part
    ) = composers.map { composer ->
        val availableSongs = composer.songs?.filteredForVocals(selectedPart.apiId)

        composer.copy(songs = availableSongs)
    }.filter {
        it.songs?.isNotEmpty() ?: false
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ComposerListState, screenName: String): ComposerListViewModel
    }

    companion object : MvRxViewModelFactory<ComposerListViewModel, ComposerListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ComposerListState
        ): ComposerListViewModel? {
            val fragment: ComposerListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.composerListViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
