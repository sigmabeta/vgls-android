package com.vgleadsheets.features.main.list.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
fun ListScreen(
    title: TitleListModel,
    listItems: List<ListModel>
) {
    VglsMaterial {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            title.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        val (icon, description) = if (title.shouldShowBack) {
                            Icons.Filled.ArrowBack to
                                stringResource(id = com.vgleadsheets.ui.components.R.string.cont_desc_app_back)
                        } else {
                            Icons.Filled.Menu to
                                stringResource(id = com.vgleadsheets.ui.components.R.string.cont_desc_app_menu)
                        }

                        IconButton(onClick = title.onMenuButtonClick) {
                            Icon(
                                imageVector = icon,
                                contentDescription = description
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            },
            content = { innerPadding ->
                LazyColumn(
                    contentPadding = innerPadding,
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    items(
                        items = listItems.toTypedArray(),
                        key = { it.dataId },
                        contentType = { it.layoutId() }
                    ) {
                        it.Content(
                            modifier = Modifier
                        )
                    }
                }
            }
        )
    }
}
