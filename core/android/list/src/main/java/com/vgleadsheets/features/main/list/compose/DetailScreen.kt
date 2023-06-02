package com.vgleadsheets.features.main.list.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.android.material.math.MathUtils
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.composables.HeaderImage
import com.vgleadsheets.composables.utils.partialLerpAfter
import com.vgleadsheets.composables.utils.partialLerpUntil
import com.vgleadsheets.themes.VglsMaterial

private val HEIGHT_HEADER_MAX = 256.dp
private val HEIGHT_HEADER_MIN = 72.dp
private val HEIGHT_VARIANCE_RANGE = HEIGHT_HEADER_MAX - HEIGHT_HEADER_MIN

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
fun DetailScreen(
    title: TitleListModel,
    listItems: List<ListModel>
) {
    VglsMaterial {
        with(LocalDensity.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val listState = rememberLazyListState()
                val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

                val expandRatio = if (firstVisibleItemIndex > 0) {
                    0.0f
                } else {
                    val firstVisibleItemScrollOffset by remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }
                    val fullCollapsePixels = HEIGHT_VARIANCE_RANGE.toPx()

                    val diff = fullCollapsePixels - firstVisibleItemScrollOffset

                    (diff / HEIGHT_VARIANCE_RANGE.toPx()).coerceIn(0.0f, 1.0f)
                }

                LazyColumn(
                    contentPadding = PaddingValues(top = HEIGHT_HEADER_MIN),
                    state = listState,
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxSize()
                ) {
                    item(
                        key = Long.MAX_VALUE,
                        contentType = Long.MAX_VALUE,
                    ) {
                        Spacer(modifier = Modifier.height(HEIGHT_VARIANCE_RANGE))
                    }

                    items(
                        items = listItems.toTypedArray(),
                        key = { it.dataId },
                        contentType = { it.layoutId }
                    ) {
                        it.Content(
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }

                val appBarHeight = lerp(HEIGHT_HEADER_MIN, HEIGHT_HEADER_MAX, expandRatio)
                val imageAlpha = MathUtils.lerp(0.2f, 1.0f, expandRatio)
                val bigTextAlpha = partialLerpUntil(0.0f, 1.0f, expandRatio, 0.5f)
                val smallTextAlpha = partialLerpAfter(1.0f, 0.0f, expandRatio, 0.5f)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(appBarHeight)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    HeaderImage(
                        imageUrl = title.photoUrl,
                        imagePlaceholder = com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(imageAlpha)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(HEIGHT_HEADER_MIN)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        IconButton(onClick = title.onMenuButtonClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.cont_desc_app_back),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        Text(
                            text = title.title,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1.0f)
                                .alpha(smallTextAlpha)
                        )

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "choose part",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = R.string.label_search),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .alpha(bigTextAlpha)
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0, 0, 0, 0),
                                        Color(0, 0, 0, 32),
                                        Color(0, 0, 0, 64),
                                        Color(0, 0, 0, 112),
                                        Color(0, 0, 0, 160),
                                    )
                                )
                            )
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = title.title,
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.titleLarge.copy(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GameDetailScreen() {
    Box(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        Sample()
    }
}

@Composable
private fun Sample() {
    DetailScreen(
        title = TitleListModel(
            "Xenoblade Chronicles 3",
            "15 songs",
            false,
            true,
            true,
            photoUrl = "https://randomfox.ca/images/96.jpg",
            placeholder = R.drawable.img_preview_game,
            onMenuButtonClick = { },
            onImageLoadSuccess = { },
            onImageLoadFail = { },
        ),
        listItems = listOf(
            CtaListModel(
                com.vgleadsheets.vectors.R.drawable.ic_jam_filled,
                "Remove from favorites",
                onClick = { },
            ),
            SectionHeaderListModel(
                "Songs"
            ),
            ImageNameCaptionListModel(
                1234L,
                "Chain Attack",
                "Kenji Hiramatsu",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                2345L,
                "Iris Network",
                "Manami Kiyota",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                3456L,
                "Origin",
                "Yasunori Mitsuda",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                4567L,
                "Moebius Battle",
                "ACE+",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                12134L,
                "Chain Attack",
                "Kenji Hiramatsu",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                23145L,
                "Iris Network",
                "Manami Kiyota",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                34156L,
                "Origin",
                "Yasunori Mitsuda",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                45167L,
                "Moebius Battle",
                "ACE+",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                12234L,
                "Chain Attack",
                "Kenji Hiramatsu",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                23245L,
                "Iris Network",
                "Manami Kiyota",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                34256L,
                "Origin",
                "Yasunori Mitsuda",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
            ImageNameCaptionListModel(
                45267L,
                "Moebius Battle",
                "ACE+",
                null,
                com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                actionableId = null,
                onClick = { },
            ),
        )
    )
}
