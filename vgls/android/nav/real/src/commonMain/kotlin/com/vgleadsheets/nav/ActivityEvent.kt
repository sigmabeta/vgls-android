package com.vgleadsheets.nav

sealed class ActivityEvent {
    /**
     * Open a URL in the platform's browser. The event carries only the URL so this stays
     * multiplatform; the platform entry point (RemasteredActivity on Android, DesktopMain on JVM)
     * builds the actual launcher (an ACTION_VIEW Intent / Desktop.browse).
     */
    data class LaunchUrl(val url: String) : ActivityEvent()
    data object Finish : ActivityEvent()
    data object Restart : ActivityEvent()
}
