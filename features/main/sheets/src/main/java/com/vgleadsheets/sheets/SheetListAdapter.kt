package com.vgleadsheets.sheets

import android.view.View
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.BaseArrayAdapter
import com.vgleadsheets.recyclerview.ListView

class SheetListAdapter(view: ListView) : BaseArrayAdapter<Song, SheetViewHolder>(view) {
    override fun createViewHolder(view: View, viewType: Int) = SheetViewHolder(view, this)

    override fun bind(holder: SheetViewHolder, item: Song) = holder.bind(item)

    override fun getLayoutId(item: Song?) = R.layout.list_item_sheet
}
