package com.vgleadsheets

import com.vgleadsheets.network.FakeVglsApi
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Before

abstract class AsyncUiTest : UiTest() {
    protected val digestEmitTrigger = MutableSharedFlow<Long>()

    protected val updateTimeEmitTrigger = MutableSharedFlow<Long>()

    @Before
    override fun setup() {
        super.setup()

        eraseDatabase()

        val api = vglsApi as FakeVglsApi

        api.digestEmitTrigger = digestEmitTrigger
        api.updateTimeEmitTrigger = updateTimeEmitTrigger
        api.generateEmptyState = false
    }

    protected fun waitForUi() {
        Thread.sleep(1000L)
    }

    protected fun emitDataFromApi() {
        updateTimeEmitTrigger.emit(0L)
        Thread.sleep(33L)
        digestEmitTrigger.emit(0L)
    }

    protected fun eraseDatabase() {
        hatchet.d(this.javaClass.simpleName, "Erasing contents of database.")

        vglsDatabase.composerDao().nukeTable()
        vglsDatabase.composerAliasDao().nukeTable()
        vglsDatabase.dbStatisticsDao().nukeTable()
        vglsDatabase.gameAliasDao().nukeTable()
        vglsDatabase.gameDao().nukeTable()
        vglsDatabase.songHistoryEntryDao().nukeTable()
        vglsDatabase.setlistEntryDao().nukeTable()
        vglsDatabase.songComposerDao().nukeTable()
        vglsDatabase.songDao().nukeTable()
        vglsDatabase.songTagValueDao().nukeTable()
        vglsDatabase.tagKeyDao().nukeTable()
        vglsDatabase.tagValueDao().nukeTable()
    }
}
