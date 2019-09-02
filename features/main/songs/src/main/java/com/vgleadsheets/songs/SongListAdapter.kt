package com.vgleadsheets.songs

import android.view.View
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class SongListAdapter(val view: SongListFragment) : BaseArrayAdapter<Song, SongViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = SongViewHolder(view, this)

    override fun bind(holder: SongViewHolder, item: Song) = holder.bind(item)

    override fun getLayoutId(item: Song?) = R.layout.list_item_sheet

    override fun onItemClick(position: Int) = view.onItemClick(dataset!![position].id)
}
