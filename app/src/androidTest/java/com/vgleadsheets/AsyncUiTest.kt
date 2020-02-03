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

        vglsDatabase.composerDao().nukeTable()
        vglsDatabase.composerAliasDao().nukeTable()
        vglsDatabase.dbStatisticsDao().nukeTable()
        vglsDatabase.gameAliasDao().nukeTable()
        vglsDatabase.gameDao().nukeTable()
        vglsDatabase.jamDao().nukeTable()
        vglsDatabase.pageDao().nukeTable()
        vglsDatabase.partDao().nukeTable()
        vglsDatabase.setlistEntryDao().nukeTable()
        vglsDatabase.songComposerDao().nukeTable()
        vglsDatabase.songDao().nukeTable()
        vglsDatabase.songHistoryEntryDao().nukeTable()
        vglsDatabase.songTagValueDao().nukeTable()
        vglsDatabase.tagKeyDao().nukeTable()
        vglsDatabase.tagValueDao().nukeTable()

        val api = vglsApi as MockVglsApi

        api.digestEmitTrigger = digestEmitTrigger
        api.updateTimeEmitTrigger = updateTimeEmitTrigger
    }
}
