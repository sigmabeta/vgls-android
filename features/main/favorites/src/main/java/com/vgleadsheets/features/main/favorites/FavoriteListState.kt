package com.vgleadsheets.features.main.favorites

import com.vgleadsheets.features.main.list.CompositeState

data class FavoriteListState(
    override val contentLoad: FavoriteListContent = FavoriteListContent(),
) : CompositeState<FavoriteListContent>
