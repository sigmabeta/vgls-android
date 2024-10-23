package com.vgleadsheets.images

import coil3.util.Logger
import com.vgleadsheets.logging.Hatchet
import javax.inject.Inject

class HatchetCoilLogger @Inject constructor(private val hatchet: Hatchet) : Logger {
    override var minLevel = Logger.Level.Verbose

    override fun log(tag: String, level: Logger.Level, message: String?, throwable: Throwable?) {
        hatchet.log(level.ordinal + 2, message ?: throwable?.message ?: "Somehow a blank message")
    }
}
