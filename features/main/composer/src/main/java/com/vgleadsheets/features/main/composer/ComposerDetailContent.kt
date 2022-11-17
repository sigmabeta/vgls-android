package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.ListContent
import com.vgleadsheets.features.main.list.failure
import com.vgleadsheets.features.main.list.hasFailed
import com.vgleadsheets.features.main.list.isLoading
import com.vgleadsheets.features.main.list.isReady
import com.vgleadsheets.model.Composer

data class ComposerDetailContent(
    val composer: Async<Composer> = Uninitialized,
) : ListContent {
    override fun failure() = composer.failure()

    override fun isLoading() = composer.isLoading()

    override fun hasFailed() = composer.hasFailed()

    override fun isFullyLoaded() = composer.isReady()

    override fun isReady() = composer.isReady()

    override fun isEmpty() = composer()?.songs?.isEmpty() == true
}
