package com.vgleadsheets.tracking

import android.app.Activity

class NoopTracker : Tracker {
    override fun logScreenView(activity: Activity, screenName: String) = Unit
    override fun logMenuShow() = Unit
    override fun logForceRefresh() = Unit
    override fun logSearch(query: String) = Unit
    override fun logPartClicked(name: String) = Unit
}
