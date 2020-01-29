package com.vgleadsheets

import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View?>? {
        return atPositionOnView(position)
    }

    private fun atPositionOnView(position: Int): Matcher<View?>? {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()

                if (resources != null) {
                    idDescription = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (ex: NotFoundException) {
                        "$recyclerViewId (resource name not found)"
                    }
                }

                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {
                resources = view.resources

                val recycler = view.rootView.findViewById(recyclerViewId) as RecyclerView

                val childView =
                    if (recycler.id == recyclerViewId) {
                        recycler.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }

                return view === childView
            }
        }
    }
}
