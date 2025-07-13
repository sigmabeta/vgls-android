package com.vgleadsheets.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.VglsMaterialVectors

val VglsMaterialVectors.IcCloudDone24dp: ImageVector
    get() {
        if (_icCloudDone24dp != null) {
            return _icCloudDone24dp!!
        }
        _icCloudDone24dp = ImageVector.Builder(
            name = "IcCloudDone24dp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19.35f, 10.04f)
                curveTo(18.67f, 6.59f, 15.64f, 4f, 12f, 4f)
                curveTo(9.11f, 4f, 6.6f, 5.64f, 5.35f, 8.04f)
                curveTo(2.34f, 8.36f, 0f, 10.91f, 0f, 14f)
                curveToRelative(0f, 3.31f, 2.69f, 6f, 6f, 6f)
                horizontalLineToRelative(13f)
                curveToRelative(2.76f, 0f, 5f, -2.24f, 5f, -5f)
                curveToRelative(0f, -2.64f, -2.05f, -4.78f, -4.65f, -4.96f)
                close()
                moveTo(10f, 17f)
                lineToRelative(-3.5f, -3.5f)
                lineToRelative(1.41f, -1.41f)
                lineTo(10f, 14.17f)
                lineTo(15.18f, 9f)
                lineToRelative(1.41f, 1.41f)
                lineTo(10f, 17f)
                close()
            }
        }.build()

        return _icCloudDone24dp!!
    }

@Suppress("ObjectPropertyName")
private var _icCloudDone24dp: ImageVector? = null
