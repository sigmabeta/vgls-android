package com.vgleadsheets.list

import com.vgleadsheets.nav.Destination
import kotlinx.coroutines.CoroutineScope

interface BrainProvider {
    fun provideBrain(destination: Destination, coroutineScope: CoroutineScope): ListViewModelBrain
}
