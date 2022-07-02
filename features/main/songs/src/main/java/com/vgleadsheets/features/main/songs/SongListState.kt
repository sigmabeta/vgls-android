package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.list.BetterCompositeState

data class SongListState(
    override val contentLoad: SongListContent = SongListContent(),
) : BetterCompositeState<SongListContent>
