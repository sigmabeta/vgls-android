package com.vgleadsheets.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.vgleadsheets.model.ListItem
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T : ListItem<T>, VH : BaseViewHolder<T, VH, A>, A : BaseArrayAdapter<T, VH>>(
        val view: View,
        val adapter: A) : RecyclerView.ViewHolder(view), View.OnClickListener, LayoutContainer {
    init {
        view.setOnClickListener(this)
    }

    override fun onClick(clicked: View) {
        adapter.onItemClick(adapterPosition)
    }

    override val containerView: View?
        get() = view

    abstract fun bind(toBind: T)
}