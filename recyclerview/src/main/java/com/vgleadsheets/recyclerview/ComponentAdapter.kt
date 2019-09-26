package com.vgleadsheets.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vgleadsheets.components.ComponentViewHolder
import com.vgleadsheets.components.ItemProvider
import com.vgleadsheets.components.ListModel

@Suppress("TooManyFunctions")
class ComponentAdapter :
    ItemProvider,
    ListAdapter<ListModel, ComponentViewHolder>(object : DiffUtil.ItemCallback<ListModel>() {
        override fun areItemsTheSame(oldItem: ListModel, newItem: ListModel) = oldItem.dataId == newItem.dataId

        // Impls should be data classes, it's fiiiine
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ListModel, newItem: ListModel) = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)

        return ComponentViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) = holder.bind(getItem(position))

    override fun getItemAtPosition(position: Int): ListModel = getItem(position)

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = getItem(position).layoutId
}
