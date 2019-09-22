package com.vgleadsheets.args

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class SongListArgs(open val id: Long?) : Parcelable

@Parcelize
data class SongsByGameArgs(override val id: Long) : SongListArgs(id)

@Parcelize
data class SongsByComposerArgs(override val id: Long) : SongListArgs(id)

@Parcelize
data class AllSongsArgs(val ignored: Long? = null): SongListArgs(null)
