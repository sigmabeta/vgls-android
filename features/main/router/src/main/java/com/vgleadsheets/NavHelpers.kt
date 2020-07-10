package com.vgleadsheets

import android.net.Uri

fun getYoutubeSearchUrlForQuery(query: String) = Uri.Builder()
    .scheme("https")
    .authority("www.youtube.com")
    .appendPath("results")
    .appendQueryParameter("search_query", query)
    .toString()