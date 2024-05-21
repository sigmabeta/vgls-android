package com.vgleadsheets.di

import android.util.Log
import com.vgleadsheets.logging.Hatchet
import javax.inject.Inject
import okhttp3.logging.HttpLoggingInterceptor

class HatchetOkHttpLogger @Inject constructor(private val hatchet: Hatchet) : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        hatchet.log(Log.VERBOSE, message)
    }
}
