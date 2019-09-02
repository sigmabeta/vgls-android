package com.vgleadsheets.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vgleadsheets.model.ListItem
import timber.log.Timber

@Suppress("TooManyFunctions")
abstract class BaseArrayAdapter<T : ListItem<T>, VH : BaseViewHolder<*, *, *>>() :
    ListAdapter<T, VH>(object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.isTheSameAs(newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.hasSameContentAs(newItem)
    }) {
    protected var datasetInternal: List<T>? = null

    protected var diffStartTime = 0L

    var dataset: List<T>?
        get() = datasetInternal
        set(value) {
            diffStartTime = System.currentTimeMillis()
            if (value === datasetInternal) {
                Timber.i("Received existing list of size %d", value?.size ?: -1)
            } else {
                datasetInternal = value
                submitList(value)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val item = LayoutInflater.from(parent.context)?.inflate(viewType, parent, false)

        return if (item != null) {
            createViewHolder(item, viewType)
        } else {
            throw IllegalArgumentException("Unable to inflate view of type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let {
            bind(holder, it)
        } ?: let {
            Timber.e("Can't bind view; dataset is not valid.")
        }
    }

    override fun getItemCount() = datasetInternal?.size ?: 0

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = getLayoutId(getItem(position)) ?: -1

    abstract fun onItemClick(position: Int)

    abstract fun createViewHolder(view: View, viewType: Int): VH

    protected abstract fun bind(holder: VH, item: T)

    @LayoutRes
    protected abstract fun getLayoutId(item: T?): Int?
}
