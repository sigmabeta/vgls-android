package com.vgleadsheets.perf.view

import com.vgleadsheets.perf.view.common.PerfView
import com.vgleadsheets.perf.view.noop.NoopPerfView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfViewModule {
    @Provides
    @Singleton
    fun providePerfView(): PerfView = NoopPerfView()
}
