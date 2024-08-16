package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.SquareItemListModel
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
) : HomeModule(
    priority = Priority.LOW,
) {
    override fun state() = composerRepository
        .getMostSongsComposers()
        .map { composers ->
            LCE.Content(
                HomeModuleState(
                    moduleName = this.javaClass.simpleName,
                    shouldShow = composers.isNotEmpty(),
                    priority = priority,
                    title = stringProvider.getString(StringId.HOME_SECTION_MOST_SONGS_COMPOSERS),
                    items = composers.map { composer ->
                        SquareItemListModel(
                            dataId = composer.id,
                            name = composer.name,
                            sourceInfo = composer.photoUrl,
                            imagePlaceholder = Icon.PERSON,
                            clickAction = Action.MostSongsComposerClicked(composer.id)
                        )
                    }
                )
            )
        }
        .withLoadingState()
        .withErrorState()
}
