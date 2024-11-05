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

internal val VglsMaterialVectors.IcCrossOutError24dp: ImageVector
    get() {
        if (_icCrossOutError24dp != null) {
            return _icCrossOutError24dp!!
        }
        _icCrossOutError24dp = Builder(
            name = "IcCrossOutError24dp", defaultWidth = 13.0.dp,
            defaultHeight = 13.0.dp, viewportWidth = 100.0f, viewportHeight = 100.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(13.0f, 17.0f)
                lineTo(83.0f, 87.0f)
                lineTo(87.0f, 83.0f)
                lineTo(17.0f, 13.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(13.0f, 17.0f)
                lineTo(83.0f, 87.0f)
                lineTo(79.0f, 91.0f)
                lineTo(9.0f, 21.0f)
                close()
            }
        }
            .build()
        return _icCrossOutError24dp!!
    }

private var _icCrossOutError24dp: ImageVector? = null
