package com.vgleadsheets.logging

import android.util.Log

class AndroidHatchet : Hatchet {
    override fun v(tag: String, message: String) {
        log(Log.VERBOSE, tag, message)
    }

    override fun d(tag: String, message: String) {
        log(Log.DEBUG, tag, message)
    }

    override fun i(tag: String, message: String) {
        log(Log.INFO, tag, message)
    }

    override fun w(tag: String, message: String) {
        log(Log.WARN, tag, message)
    }

    override fun e(tag: String, message: String) {
        log(Log.ERROR, tag, message)
    }

    override fun log(severity: Int, tag: String, message: String) {
        logInternal(severity, tag, message)
    }

    private fun logInternal(severity: Int, tag: String, message: String) {
        val threadName = Thread.currentThread().name
        val string = "Thr: $threadName | Msg: $message"

        Log.println(
            severity,
            tag,
            string
        )
    }
}
