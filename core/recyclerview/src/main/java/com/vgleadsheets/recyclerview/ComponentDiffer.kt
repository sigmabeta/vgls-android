package com.vgleadsheets.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.vgleadsheets.components.ListModel

object ComponentDiffer : DiffUtil.ItemCallback<ListModel>() {
    override fun areItemsTheSame(oldItem: ListModel, newItem: ListModel) =
        oldItem.dataId == newItem.dataId

    // Impls should be data classes, it's fiiiine
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListModel, newItem: ListModel) = oldItem == newItem

    override fun getChangePayload(oldItem: ListModel, newItem: ListModel) = true
}
