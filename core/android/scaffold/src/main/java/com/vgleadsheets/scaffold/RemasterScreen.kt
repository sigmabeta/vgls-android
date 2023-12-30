package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.composables.NameCaptionListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemasterScreen(
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
    ) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = "home"
        ) {
            composable("home") {
                NameCaptionListItem(
                    model = NameCaptionListModel(
                        dataId = 1234L,
                        name = "Just a home screen",
                        caption = "Nothing to see here",
                        onClick = { }
                    ),
                    modifier = Modifier
                )
            }
        }
    }
}
