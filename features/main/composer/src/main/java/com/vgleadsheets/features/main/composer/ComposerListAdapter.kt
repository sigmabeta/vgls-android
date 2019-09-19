package com.vgleadsheets.features.main.composer

import android.view.View
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class ComposerListAdapter(val view: ComposerListFragment) : BaseArrayAdapter<Composer, ComposerViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = ComposerViewHolder(view, this)

    override fun bind(holder: ComposerViewHolder, item: Composer) = holder.bind(item)

    override fun getLayoutId(item: Composer?) = R.layout.list_item_composer

    override fun onItemClick(position: Int) = view.onItemClick(dataset!![position].id)
}
