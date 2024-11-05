package com.vgleadsheets.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Bevel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.VglsMaterialVectors

internal val VglsMaterialVectors.IcJamUnfilled: ImageVector
    get() {
        if (_icJamUnfilled != null) {
            return _icJamUnfilled!!
        }
        _icJamUnfilled = Builder(
            name = "IcJamUnfilled", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 48.0f, viewportHeight = 48.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveToRelative(14.0f, 14.0f)
                curveToRelative(-2.0f, 1.0f, -4.9721f, 4.3255f, -5.0f, 8.0f)
                verticalLineToRelative(13.0f)
                curveToRelative(0.0f, 5.0f, 3.0f, 8.0f, 7.0f, 8.0f)
                horizontalLineToRelative(16.0f)
                curveToRelative(4.0f, 0.0f, 7.0f, -3.0f, 7.0f, -8.0f)
                verticalLineTo(22.0f)
                curveToRelative(0.0f, -3.7408f, -3.0f, -7.0f, -5.0f, -8.0f)
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin = Bevel,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveToRelative(15.0f, 10.0f)
                curveToRelative(-2.0f, 0.0f, -2.0f, 0.0f, -3.0f, 1.0f)
                curveToRelative(0.0f, 0.0f, -5.1704f, 3.7385f, -3.964f, 3.9353f)
                curveTo(9.2423f, 15.1322f, 12.8254f, 14.7876f, 14.0f, 14.0f)
                curveToRelative(1.9959f, -1.3382f, 1.8493f, -1.0753f, 4.0f, 0.0f)
                curveToRelative(2.0f, 1.0f, 2.0f, 1.0f, 4.0f, 0.0f)
                curveToRelative(2.0f, -1.0f, 2.0f, -1.0f, 4.0f, 0.0f)
                curveToRelative(2.0f, 1.0f, 2.0f, 1.0f, 4.0f, 0.0f)
                curveToRelative(2.0f, -1.0f, 2.0f, -1.0f, 4.0f, 0.0f)
                curveToRelative(1.2649f, 0.6325f, 4.773f, 1.2169f, 5.9568f, 1.0588f)
                curveTo(41.1406f, 14.9007f, 36.0f, 11.0f, 36.0f, 11.0f)
                curveTo(35.0f, 10.0f, 35.0f, 10.0f, 33.0f, 10.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(15.0f, 6.0f)
                lineTo(33.0f, 6.0f)
                arcTo(2.0f, 1.5f, 0.0f, false, true, 35.0f, 7.5f)
                lineTo(35.0f, 7.5f)
                arcTo(2.0f, 1.5f, 0.0f, false, true, 33.0f, 9.0f)
                lineTo(15.0f, 9.0f)
                arcTo(2.0f, 1.5f, 0.0f, false, true, 13.0f, 7.5f)
                lineTo(13.0f, 7.5f)
                arcTo(2.0f, 1.5f, 0.0f, false, true, 15.0f, 6.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.99870706f, strokeLineCap = Round, strokeLineJoin = Bevel,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveToRelative(34.5065f, 9.5f)
                horizontalLineToRelative(4.9871f)
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.99779046f, strokeLineCap = Round, strokeLineJoin = Bevel,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveToRelative(39.4954f, 6.0032f)
                lineToRelative(-3.9908f, 2.9936f)
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000080)),
                strokeLineWidth = 0.0f, strokeLineCap = Round, strokeLineJoin = Bevel,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(13.0f, 20.0f)
                lineTo(13.0f, 20.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 14.0f, 21.0f)
                lineTo(14.0f, 27.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 13.0f, 28.0f)
                lineTo(13.0f, 28.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 12.0f, 27.0f)
                lineTo(12.0f, 21.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 13.0f, 20.0f)
                close()
            }
        }
            .build()
        return _icJamUnfilled!!
    }

private var _icJamUnfilled: ImageVector? = null
