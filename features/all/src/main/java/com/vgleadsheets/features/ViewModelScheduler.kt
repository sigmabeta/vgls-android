package com.vgleadsheets.features

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.list.VglsScheduler
import kotlinx.coroutines.CoroutineScope

class ViewModelScheduler(
    override val coroutineScope: CoroutineScope,
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
) : VglsScheduler
