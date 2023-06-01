package com.vgleadsheets.images

import android.util.Log
import coil.util.Logger
import com.vgleadsheets.logging.Hatchet
import javax.inject.Inject

class HatchetCoilLogger @Inject constructor(private val hatchet: Hatchet): Logger {
    override var level = Log.VERBOSE

    override fun log(tag: String, priority: Int, message: String?, throwable: Throwable?) {
        hatchet.log(priority, tag,message ?: throwable?.message ?: "Somehow a blank message")
    }
}
