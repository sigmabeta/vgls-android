package com.vgleadsheets

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class RecyclerViewMatcher() {
    fun atPosition(position: Int): Matcher<View?>? {
        return atPositionOnView(position)
    }

    private fun atPositionOnView(position: Int): Matcher<View?>? {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("which is RecyclerView")
            }

            override fun matchesSafely(view: View?): Boolean {
                val rootView = view?.rootView ?: return false
                val recycler = searchChildrenForRecyclerView(rootView)

                val childView = if (recycler != null) {
                    recycler.findViewHolderForAdapterPosition(position)?.itemView
                } else {
                    return false
                }

                return view === childView
            }

            private fun searchChildrenForRecyclerView(view: View): RecyclerView? {
                if (view is RecyclerView) {
                    return view
                }

                if (view is ViewGroup) {
                    for (childIndex in 0 until view.childCount) {
                        val child = searchChildrenForRecyclerView(
                            view.getChildAt(childIndex)
                        )

                        if (child is RecyclerView) {
                            return child
                        }
                    }
                }

                return null
            }
        }
    }
}
