package com.vgleadsheets.scaffold

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier

/**
 * Code copied from AndroidX's NavSuiteScaffold class, since so much of it is private.
 */

@Composable
internal fun NavigationItemIcon(
    icon: @Composable () -> Unit,
    badge: (@Composable () -> Unit)? = null,
) {
    if (badge != null) {
        BadgedBox(badge = { badge.invoke() }) { icon() }
    } else {
        icon()
    }
}

internal interface NavigationSuiteItemProvider {
    val itemsCount: Int
    val itemList: MutableVector<NavigationSuiteItem>
}

internal class NavigationSuiteItem(
    val selected: Boolean,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
    val modifier: Modifier,
    val enabled: Boolean,
    val label: @Composable (() -> Unit)?,
    val alwaysShowLabel: Boolean,
    val badge: (@Composable () -> Unit)?,
    val colors: NavigationSuiteItemColors?,
    val interactionSource: MutableInteractionSource?
)

@Composable
internal fun rememberStateOfItems(
    content: NavigationSuiteScope.() -> Unit
): State<NavigationSuiteItemProvider> {
    val latestContent = rememberUpdatedState(content)
    return remember { derivedStateOf { NavigationSuiteScopeImpl().apply(latestContent.value) } }
}

sealed interface NavigationSuiteScope {

    /**
     * This function sets the parameters of the default Material navigation item to be used with the
     * Navigation Suite Scaffold. The item is called in [NavigationSuite], according to the current
     * [NavigationSuiteType].
     *
     * For specifics about each item component, see [NavigationBarItem], [NavigationRailItem], and
     * [NavigationDrawerItem].
     *
     * @param selected whether this item is selected
     * @param onClick called when this item is clicked
     * @param icon icon for this item, typically an [Icon]
     * @param modifier the [Modifier] to be applied to this item
     * @param enabled controls the enabled state of this item. When `false`, this component will not
     *   respond to user input, and it will appear visually disabled and disabled to accessibility
     *   services. Note: as of now, for [NavigationDrawerItem], this is always `true`.
     * @param label the text label for this item
     * @param alwaysShowLabel whether to always show the label for this item. If `false`, the label
     *   will only be shown when this item is selected. Note: for [NavigationDrawerItem] this is
     *   always `true`
     * @param badge optional badge to show on this item
     * @param colors [NavigationSuiteItemColors] that will be used to resolve the colors used for
     *   this item in different states. If null, [NavigationSuiteDefaults.itemColors] will be used.
     * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
     *   emitting [Interaction]s for this item. You can use this to change the item's appearance or
     *   preview the item in different states. Note that if `null` is provided, interactions will
     *   still happen internally.
     */
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        label: @Composable (() -> Unit)? = null,
        alwaysShowLabel: Boolean = true,
        badge: (@Composable () -> Unit)? = null,
        colors: NavigationSuiteItemColors? = null,
        interactionSource: MutableInteractionSource? = null
    )
}

internal class NavigationSuiteScopeImpl : NavigationSuiteScope, NavigationSuiteItemProvider {

    override fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        label: @Composable (() -> Unit)?,
        alwaysShowLabel: Boolean,
        badge: (@Composable () -> Unit)?,
        colors: NavigationSuiteItemColors?,
        interactionSource: MutableInteractionSource?
    ) {
        itemList.add(
            NavigationSuiteItem(
                selected = selected,
                onClick = onClick,
                icon = icon,
                modifier = modifier,
                enabled = enabled,
                label = label,
                alwaysShowLabel = alwaysShowLabel,
                badge = badge,
                colors = colors,
                interactionSource = interactionSource
            )
        )
    }

    override val itemList: MutableVector<NavigationSuiteItem> = mutableVectorOf()

    override val itemsCount: Int
        get() = itemList.size
}
