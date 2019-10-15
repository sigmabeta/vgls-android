package com.vgleadsheets.tracking

import android.app.Activity

interface Tracker {
    fun logScreenView(activity: Activity, screenName: String)
    fun logMenuShow()
    fun logForceRefresh()
    fun logSearch(query: String)
}
