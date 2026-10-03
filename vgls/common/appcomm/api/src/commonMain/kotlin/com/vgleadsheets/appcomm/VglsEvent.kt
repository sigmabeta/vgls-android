package com.vgleadsheets.appcomm

import net.sigmabeta.sage.appcomm.SageEvent

open class VglsEvent : SageEvent() {
    data object RefreshDb : VglsEvent()

    data class SearchYoutubeClicked(val query: String) : VglsEvent()

    data object GiantBombLinkClicked : VglsEvent()
    data object WebsiteLinkClicked : VglsEvent()
    data object PrivacyLinkClicked : VglsEvent()

    /** Open [url] in the platform's browser, e.g. a photo's source page for attribution. */
    data class OpenUrl(val url: String) : VglsEvent()

    data object RestartApp : VglsEvent()
}
