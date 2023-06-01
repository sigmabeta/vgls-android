package com.vgleadsheets.di

import android.util.Log
import com.vgleadsheets.logging.Hatchet
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class HatchetOkHttpLogger @Inject constructor(private val hatchet: Hatchet): HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        hatchet.log(Log.VERBOSE, "OkHttp", message)
    }
}
