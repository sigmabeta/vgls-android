package com.vgleadsheets.search

import android.view.View
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.recyclerview.BaseArrayAdapter
import com.vgleadsheets.recyclerview.ListView

class SearchResultAdapter(view: ListView) : BaseArrayAdapter<SearchResult, SearchResultViewHolder>(view) {
    override fun createViewHolder(view: View, viewType: Int) = SearchResultViewHolder(view, this)

    override fun bind(holder: SearchResultViewHolder, item: SearchResult) = holder.bind(item)

    override fun getLayoutId(item: SearchResult?) = R.layout.list_item_search_result
}

