package com.vgleadsheets.features.main.favorites

import com.vgleadsheets.features.main.list.BetterCompositeState

data class FavoriteListState(
    override val contentLoad: FavoriteListContent = FavoriteListContent(),
) : BetterCompositeState<FavoriteListContent>
