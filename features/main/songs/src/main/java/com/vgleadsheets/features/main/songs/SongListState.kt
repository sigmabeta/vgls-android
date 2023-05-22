package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.list.CompositeState

data class SongListState(
    override val contentLoad: SongListContent = SongListContent(),
) : CompositeState<SongListContent>
