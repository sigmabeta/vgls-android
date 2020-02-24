package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.GiantBombImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.game.Game

data class GameListState(
    val games: Async<List<Game>> = Uninitialized,
    val updateTime: Async<*> = Uninitialized,
    val digest: Async<*> = Uninitialized,
    val selectedPart: PartSelectorItem? = null,
    val clickedGbListModel: GiantBombImageNameCaptionListModel? = null,
    val gbApiNotAvailable: Boolean? = null,
    val listModels: List<ListModel> = emptyList()
) : MvRxState
