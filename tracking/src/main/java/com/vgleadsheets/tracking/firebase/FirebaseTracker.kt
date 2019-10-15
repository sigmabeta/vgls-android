package com.vgleadsheets.tracking.firebase

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.tracking.Tracker
import timber.log.Timber

class FirebaseTracker(val firebaseAnalytics: FirebaseAnalytics) : Tracker {

    override fun logScreenView(activity: Activity, screenName: String) {
        Timber.d("Logging screenview: $screenName")
        firebaseAnalytics.setCurrentScreen(activity, screenName, screenName.substringBefore(":"))
    }

    override fun logMenuShow() {
        Timber.d("Logging ShowMenu")
        firebaseAnalytics.logEvent("ShowMenu", null)
    }

    override fun logForceRefresh() {
        Timber.d("Logging ForceRefresh")
        firebaseAnalytics.logEvent("ForceRefresh", null)
    }

    override fun logSearch(query: String) {
        Timber.d("Logging search: $query")

        val details = Bundle()
        details.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, details)
    }
}
