package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

class TagKeyViewModel @AssistedInject constructor(
    @Assisted initialState: TagKeyState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : ListViewModel<TagKey, TagKeyState>(initialState),
    NameCaptionListModel.EventHandler {
    init {
        fetchTagKeys()
    }

    override fun onClicked(clicked: NameCaptionListModel) = setState {
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
        val spec = PerfSpec.TAG_KEY

        perfTracker.onTitleLoaded(spec)
        perfTracker.onTransitionStarted(spec)

        return TitleListModel(
            resourceProvider.getString(R.string.app_name),
            resourceProvider.getString(R.string.subtitle_tags),
        )
    }

    override fun defaultLoadingListModel(index: Int): ListModel =
        LoadingNameCaptionListModel("allData", index)

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No tags found at all. Check your internet connection?",
    )

    override fun createSuccessListModels(
        data: List<TagKey>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: Part
    ): List<NameCaptionListModel> {
        val spec = PerfSpec.TAG_KEY

        perfTracker.onPartialContentLoad(spec)
        perfTracker.onFullContentLoad(spec)

        return data.map {
            NameCaptionListModel(
                it.id,
                it.name,
                generateSubtitleText(it.values),
                this@TagKeyViewModel,
            )
        }
    }

    private fun fetchTagKeys() {
        repository.getAllTagKeys()
            .execute { tagKeys ->
                updateListState(
                    data = tagKeys,
                    listModels = constructList(
                        tagKeys,
                        updateTime,
                        digest,
                        selectedPart
                    )
                )
            }
    }

    @Suppress("LoopWithTooManyJumpStatements")
    private fun generateSubtitleText(items: List<TagValue>?): String {
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
                resourceProvider.getString(R.string.subtitle_suffix_others, numberOfOthers)
            )
        }

        return builder.toString()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: TagKeyState, screenName: String): TagKeyViewModel
    }

    companion object : MvRxViewModelFactory<TagKeyViewModel, TagKeyState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TagKeyState
        ): TagKeyViewModel? {
            val fragment: TagKeyFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.tagKeyViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
