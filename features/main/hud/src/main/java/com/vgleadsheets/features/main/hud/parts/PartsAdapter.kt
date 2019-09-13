package com.vgleadsheets.features.main.hud.parts

import android.view.View
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class PartsAdapter(val view: HudFragment) : BaseArrayAdapter<PartSelectorItem, PartViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = PartViewHolder(view, this)

    override fun bind(holder: PartViewHolder, item: PartSelectorItem) = holder.bind(item)

    override fun getLayoutId(item: PartSelectorItem?) = R.layout.list_item_part

    override fun onItemClick(position: Int) = view.onItemClick(getItem(position).apiId)
}