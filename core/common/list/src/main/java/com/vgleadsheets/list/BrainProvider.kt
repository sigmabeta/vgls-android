package com.vgleadsheets.list

import net.sigmabeta.sage.nav.Destination
import kotlinx.coroutines.CoroutineScope

interface BrainProvider {
    fun provideBrain(destination: Destination, coroutineScope: CoroutineScope): ListViewModelBrain
}
