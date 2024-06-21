package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MostSongsComposersModule @Inject constructor(
    private val composerRepository: ComposerRepository,
    private val stringProvider: StringProvider,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope,
) : HomeModule(
    dispatchers,
    coroutineScope,
) {
    override fun state() = composerRepository
        .getMostSongsComposers()
        .map { composers ->
            HomeModuleState(
                shouldShow = composers.isNotEmpty(),
                priority = Priority.HIGH,
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
        }
}
