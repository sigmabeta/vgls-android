package com.vgleadsheets.features

import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.SageScheduler

class ViewModelScheduler(
    override val coroutineScope: CoroutineScope,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
) : SageScheduler
