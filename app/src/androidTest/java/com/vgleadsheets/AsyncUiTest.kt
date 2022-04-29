package com.vgleadsheets

import com.vgleadsheets.network.MockVglsApi
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import timber.log.Timber

abstract class AsyncUiTest : UiTest() {
    protected val digestEmitTrigger = BehaviorSubject.create<Long>()

    protected val updateTimeEmitTrigger = BehaviorSubject.create<Long>()

    @Before
    override fun setup() {
        super.setup()

        eraseDatabase()

        val api = vglsApi as MockVglsApi

        api.digestEmitTrigger = digestEmitTrigger
        api.updateTimeEmitTrigger = updateTimeEmitTrigger
        api.generateEmptyState = false
    }

    protected fun waitForUi() {
        Thread.sleep(1000L)
    }

    protected fun emitDataFromApi() {
        updateTimeEmitTrigger.onNext(0L)
        Thread.sleep(33L)
        digestEmitTrigger.onNext(0L)
    }

    protected fun eraseDatabase() {
        Timber.d("Erasing contents of database.")

        vglsDatabase.composerDao().nukeTable()
        vglsDatabase.composerAliasDao().nukeTable()
        vglsDatabase.dbStatisticsDao().nukeTable()
        vglsDatabase.gameAliasDao().nukeTable()
        vglsDatabase.gameDao().nukeTable()
        vglsDatabase.songHistoryEntryDao().nukeTable()
        vglsDatabase.setlistEntryDao().nukeTable()
        vglsDatabase.jamDao().nukeTable()
        vglsDatabase.songComposerDao().nukeTable()
        vglsDatabase.songDao().nukeTable()
        vglsDatabase.songTagValueDao().nukeTable()
        vglsDatabase.tagKeyDao().nukeTable()
        vglsDatabase.tagValueDao().nukeTable()
    }
}
