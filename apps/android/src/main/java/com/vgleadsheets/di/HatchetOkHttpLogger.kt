package com.vgleadsheets.di

import android.util.Log
import net.sigmabeta.sage.logging.Hatchet
import dev.zacsweers.metro.Inject
import okhttp3.logging.HttpLoggingInterceptor

class HatchetOkHttpLogger @Inject constructor(private val hatchet: Hatchet) : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        hatchet.log(Log.VERBOSE, message)
    }
}
