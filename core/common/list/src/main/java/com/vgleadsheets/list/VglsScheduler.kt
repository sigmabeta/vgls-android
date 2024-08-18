package com.vgleadsheets.list

import com.vgleadsheets.coroutines.VglsDispatchers
import kotlinx.coroutines.CoroutineScope

interface VglsScheduler {
    val dispatchers: VglsDispatchers
    val coroutineScope: CoroutineScope
    val delayManager: DelayManager
}
