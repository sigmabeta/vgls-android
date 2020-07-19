package com.vgleadsheets.perf.tracking.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoadStatus(
    val titleLoaded: Boolean = false,
    val transitionStarted: Boolean = false,
    val contentPartiallyLoaded: Boolean = false,
    val contentFullyLoaded: Boolean = false,
    val loadFailed: Boolean = false,
    val cancelled: Boolean = false
) : Parcelable {
    fun isLoadComplete() = titleLoaded && transitionStarted && contentPartiallyLoaded && contentFullyLoaded
}