package com.vgleadsheets.nav

import android.content.Intent

sealed class ActivityEvent {
    data class LaunchIntent(val intent: Intent) : ActivityEvent()
    data object Finish : ActivityEvent()
}
