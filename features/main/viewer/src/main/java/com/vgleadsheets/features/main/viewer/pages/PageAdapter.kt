package com.vgleadsheets.features.main.viewer.pages

import android.view.View
import com.vgleadsheets.features.main.viewer.R
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class PageAdapter(val view: ViewerFragment) : BaseArrayAdapter<Page, PageViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = PageViewHolder(view, this)

    override fun bind(holder: PageViewHolder, item: Page) = holder.bind(item)

    override fun getLayoutId(item: Page?) = R.layout.list_item_page

    override fun onItemClick(position: Int) = Unit

    fun onError(adapterPosition: Int) {
        view.onPageLoadError(adapterPosition + 1)
    }
}
