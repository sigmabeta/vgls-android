package com.vgleadsheets.features

import net.sigmabeta.sage.coroutines.VglsDispatchers
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.VglsScheduler
import kotlinx.coroutines.CoroutineScope

class ViewModelScheduler(
    override val coroutineScope: CoroutineScope,
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
) : VglsScheduler
