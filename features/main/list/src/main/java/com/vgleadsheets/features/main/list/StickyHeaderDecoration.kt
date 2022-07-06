package com.vgleadsheets.features.main.list

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.vgleadsheets.components.ComponentViewHolder
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.recyclerview.ComponentAdapter

class StickyHeaderDecoration(
    private val adapter: ComponentAdapter,
    private val root: View
) : ItemDecoration() {
    private var previousLayoutId: Int = 0

    private var previousViewHolder: ComponentViewHolder? = null

    private val stickyHeader: View?
        get() = previousViewHolder?.itemView

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: State) {
        super.onDrawOver(canvas, parent, state)

        val scrollingHeader = parent.getChildAt(0)

        val headerModel = adapter.getFirstItem()
        if ((headerModel !is TitleListModel) && (headerModel !is LoadingTitleListModel)) {
            return
        }

        // Don't render if loading views are at the top.
        if (headerModel !is LoadingTitleListModel &&
            scrollingHeader.id == com.vgleadsheets.ui_core.R.id.component_loading_name_caption
        ) {
            return
        }

        // Check if the title view is at the top; if not, render the smallest version of the header.
        val progress = if (scrollingHeader.id == com.vgleadsheets.ui_core.R.id.root_title) {
            1.0f - minOf(scrollingHeader.bottom / scrollingHeader.height.toFloat(), 1.0f)
        } else {
            1.0f
        }

        val modelDupe = getDupedModel(headerModel, progress)
        val viewHolder = getCorrectViewHolder(modelDupe)

        viewHolder.bind(modelDupe)

        layoutHeaderView(scrollingHeader)
        canvas.drawHeaderView()
    }

    private fun getCorrectViewHolder(modelDupe: ListModel): ComponentViewHolder {
        val oldHolder = previousViewHolder
        return if (previousLayoutId == modelDupe.layoutId && oldHolder != null) {
            oldHolder
        } else {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(root.context),
                modelDupe.layoutId,
                null,
                false
            )

            binding.root.visibility = View.VISIBLE

            val newHolder = ComponentViewHolder(
                binding
            )

            previousViewHolder = newHolder
            newHolder
        }
    }

    private fun getDupedModel(
        headerModel: ListModel?,
        progress: Float
    ) = when (headerModel) {
        is TitleListModel -> headerModel.copy(
            shrinkPercent = progress,
            hidden = progress <= 0.0f,
            shouldSetMinHeightOnly = true
        )
        is LoadingTitleListModel -> headerModel.copy(
            shrinkPercent = progress,
            hidden = progress <= 0.0f,
            shouldSetMinHeightOnly = true
        )
        else -> throw IllegalArgumentException("HeaderModel needs to be some sort of title.")
    }

    private fun layoutHeaderView(scrollingView: View) {
        stickyHeader?.let {
            it.measure(
                MeasureSpec.makeMeasureSpec(scrollingView.width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            )
            it.layout(
                scrollingView.left,
                0,
                scrollingView.right,
                it.measuredHeight
            )
        }
    }

    private fun Canvas.drawHeaderView() {
        save()
        stickyHeader?.draw(this)
        restore()
    }
}
