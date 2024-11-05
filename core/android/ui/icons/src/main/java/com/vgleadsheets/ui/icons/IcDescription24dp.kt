package com.vgleadsheets.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.VglsMaterialVectors

internal val VglsMaterialVectors.IcDescription24dp: ImageVector
    get() {
        if (_icDescription24dp != null) {
            return _icDescription24dp!!
        }
        _icDescription24dp = Builder(
            name = "IcDescription24dp",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF999999)),
                stroke = null,
                strokeLineWidth = 0.0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(14.0f, 2.0f)
                lineTo(6.0f, 2.0f)
                curveToRelative(-1.1f, 0.0f, -1.99f, 0.9f, -1.99f, 2.0f)
                lineTo(4.0f, 20.0f)
                curveToRelative(0.0f, 1.1f, 0.89f, 2.0f, 1.99f, 2.0f)
                lineTo(18.0f, 22.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                lineTo(20.0f, 8.0f)
                lineToRelative(-6.0f, -6.0f)
                close()
                moveTo(16.0f, 18.0f)
                lineTo(8.0f, 18.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineToRelative(8.0f)
                verticalLineToRelative(2.0f)
                close()
                moveTo(16.0f, 14.0f)
                lineTo(8.0f, 14.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineToRelative(8.0f)
                verticalLineToRelative(2.0f)
                close()
                moveTo(13.0f, 9.0f)
                lineTo(13.0f, 3.5f)
                lineTo(18.5f, 9.0f)
                lineTo(13.0f, 9.0f)
                close()
            }
        }
            .build()
        return _icDescription24dp!!
    }

private var _icDescription24dp: ImageVector? = null
