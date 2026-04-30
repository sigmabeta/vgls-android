package com.vgleadsheets.nav

import android.net.Uri

internal fun getYoutubeSearchUrlForQuery(query: String) = Uri.Builder()
    .scheme("https")
    .authority("www.youtube.com")
    .appendPath("results")
    .appendQueryParameter("search_query", query)
    .toString()
