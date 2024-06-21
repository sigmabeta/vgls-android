package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.composables.subs.CrossfadeImage
import com.vgleadsheets.composables.subs.ElevatedRoundRect
import com.vgleadsheets.ui.Icon

@Composable
@Suppress("MagicNumber", "LongMethod", "UnsafeCallOnNullableType")
fun BigImage(
    model: HeroImageListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    ElevatedRoundRect(
        modifier = modifier
            .padding(padding)
            .height(320.dp)
            .fillMaxWidth()
            .clickable(onClick = { actionSink.sendAction(model.clickAction) }),
        cornerRadius = 16.dp,
    ) {
        CrossfadeImage(
            sourceInfo = model.sourceInfo,
            imagePlaceholder = model.imagePlaceholder,
            modifier = Modifier.fillMaxSize(),
        )

        if (!model.name.isNullOrEmpty() || !model.caption.isNullOrEmpty()) {
            Box {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0, 0, 0, 0),
                                    Color(0, 0, 0, 64),
                                    Color(0, 0, 0, 160),
                                )
                            )
                        )
                        .padding(16.dp)
                        .padding(top = 8.dp) // For extra scrim
                ) {
                    if (model.name.isNullOrEmpty()) {
                        Text(
                            text = model.name!!,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            ),
                        )
                    }

                    if (model.caption != null) {
                        Text(
                            text = model.caption!!,
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoadingGame() {
    BigImage(
        HeroImageListModel(
            sourceInfo = "whatever",
            imagePlaceholder = Icon.ALBUM,
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}

@Preview
@Composable
private fun SuccessGame() {
    BigImage(
        HeroImageListModel(
            sourceInfo = "whatever",
            imagePlaceholder = Icon.DESCRIPTION,
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}

@Preview
@Composable
private fun SuccessGameWithLabel() {
    BigImage(
        HeroImageListModel(
            sourceInfo = "whatever",
            imagePlaceholder = Icon.DESCRIPTION,
            name = "Xenoblade Chronicles 3",
            caption = "Pretty awesome game tbh",
            clickAction = VglsAction.Noop,
        ),
        PreviewActionSink { },
        modifier = Modifier,
        padding = PaddingValues(16.dp),
    )
}
