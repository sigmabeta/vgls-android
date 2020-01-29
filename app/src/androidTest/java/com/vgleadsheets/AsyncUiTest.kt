package com.vgleadsheets

import com.vgleadsheets.network.MockVglsApi
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before

abstract class AsyncUiTest: UiTest() {
    protected val digestEmitTrigger = BehaviorSubject.create<Long>()

    protected val updateTimeEmitTrigger = BehaviorSubject.create<Long>()

    @Before
    override fun setup() {
        super.setup()

        val api = vglsApi as MockVglsApi

        api.digestEmitTrigger = digestEmitTrigger
        api.updateTimeEmitTrigger = updateTimeEmitTrigger
    }
}
