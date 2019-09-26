package com.vgleadsheets.components

import androidx.databinding.BaseObservable

data class SheetListModel(
    val imageUrl: String,
    val status: Status,
    val listener: ImageListener,
    val type: String = TYPE_DEFAULT,
    override val dataId: Long = imageUrl.hashCode().toLong(),
    override val layoutId: Int = R.layout.list_component_sheet
) : BaseObservable(), ListModel {
    interface ImageListener {
        fun onClicked(clicked: SheetListModel)
        fun onLoadStart(imageUrl: String)
        fun onLoadSuccess(imageUrl: String)
        fun onLoadFailed(imageUrl: String, ex: Exception?)
    }

    enum class Status {
        NONE,
        LOADING,
        ERROR,
        SUCCESS
    }

    companion object {
        val TYPE_DEFAULT = SheetListModel::class.java.name
    }
}
