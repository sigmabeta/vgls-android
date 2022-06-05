package com.vgleadsheets.features.main.songs.better

import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterSongListState(
    override val contentLoad: BetterSongListContent = BetterSongListContent(),
) : BetterCompositeState<BetterSongListContent>
