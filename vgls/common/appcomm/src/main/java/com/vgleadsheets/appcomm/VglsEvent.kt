package com.vgleadsheets.appcomm

import net.sigmabeta.sage.appcomm.SageEvent

open class VglsEvent : SageEvent() {
    data object RefreshDb : VglsEvent()

    data class SearchYoutubeClicked(val query: String) : VglsEvent()

    data object GiantBombLinkClicked : VglsEvent()
    data object WebsiteLinkClicked : VglsEvent()
    data object PrivacyLinkClicked : VglsEvent()

    data object RestartApp : VglsEvent()
}
