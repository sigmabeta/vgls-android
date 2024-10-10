package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class MostSongsComposersModule @Inject constructor(
    private val composerRepository: ComposerRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.LOW,
    delayManager,
) {
    override fun loadingType() = LoadingType.SQUARE

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_MOST_SONGS_COMPOSERS)

    @Suppress("MagicNumber")
    override fun state() = composerRepository
        .getMostSongsComposers()
        .map { it.shuffled().take(10) }
        .map { composers ->
            LCE.Content(
                HomeModuleState(
                    moduleName = "MostSongsComposersModule",
                    shouldShow = composers.isNotEmpty(),
                    title = title(),
                    items = composers.map { composer ->
                        SquareItemListModel(
                            dataId = composer.id,
                            name = composer.name,
                            sourceInfo = composer.photoUrl,
                            imagePlaceholder = Icon.PERSON,
                            clickAction = Action.MostSongsComposerClicked(composer.id)
                        )
                    },
                )
            )
        }
        .withLoadingState()
        .withErrorState()
}
