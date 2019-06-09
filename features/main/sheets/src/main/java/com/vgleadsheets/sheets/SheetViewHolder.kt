package com.vgleadsheets.sheets

import android.view.View
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_sheet.view.*

class SheetViewHolder(view: View, adapter: SheetListAdapter) :
    BaseViewHolder<Song, SheetViewHolder, SheetListAdapter>(view, adapter) {
    override fun bind(toBind: Song) {
        view.text_sheet_name.text = toBind.name
        view.text_sheet_count.text = "${toBind.pageCount} pages"
    }
}