package com.vgleadsheets.songs

import android.view.View
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.BaseArrayAdapter
import com.vgleadsheets.recyclerview.ListView

class SongListAdapter(view: ListView) : BaseArrayAdapter<Song, SongViewHolder>(view) {
    override fun createViewHolder(view: View, viewType: Int) = SongViewHolder(view, this)

    override fun bind(holder: SongViewHolder, item: Song) = holder.bind(item)

    override fun getLayoutId(item: Song?) = R.layout.list_item_sheet
}
