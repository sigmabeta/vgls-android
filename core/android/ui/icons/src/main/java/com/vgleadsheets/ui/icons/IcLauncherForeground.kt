package com.vgleadsheets.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Square
import androidx.compose.ui.graphics.StrokeJoin.Companion.Bevel
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.VglsMaterialVectors

internal val VglsMaterialVectors.IcLauncherForeground: ImageVector
    get() {
        if (_icLauncherForeground != null) {
            return _icLauncherForeground!!
        }
        _icLauncherForeground = Builder(
            name = "IcLauncherForeground", defaultWidth = 108.0.dp,
            defaultHeight = 108.0.dp, viewportWidth = 16.933f, viewportHeight = 16.933f
        ).apply {
            group {
                path(
                    fill = SolidColor(Color(0xFF292929)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveToRelative(3.425f, 2.117f)
                    horizontalLineToRelative(10.083f)
                    arcToRelative(0.25f, 0.25f, 45.0f, false, true, 0.25f, 0.25f)
                    verticalLineTo(13.508f)
                    arcToRelative(0.604f, 0.604f, 112.5f, false, true, -0.177f, 0.427f)
                    lineToRelative(-0.705f, 0.705f)
                    arcTo(0.604f, 0.604f, 157.5f, false, true, 12.45f, 14.817f)
                    lineToRelative(-9.008f, -0.0f)
                    curveTo(3.294f, 14.817f, 3.175f, 14.697f, 3.175f, 14.55f)
                    verticalLineTo(2.367f)
                    curveToRelative(0.0f, -0.138f, 0.112f, -0.25f, 0.25f, -0.25f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFffffff)), stroke = SolidColor(Color(0xFF000000)),
                    fillAlpha = 0.239345f, strokeLineWidth = 0.0f, strokeLineCap = Butt,
                    strokeLineJoin = Bevel, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveToRelative(3.425f, 2.117f)
                    curveToRelative(-0.092f, 0.0f, -0.172f, 0.05f, -0.215f, 0.124f)
                    horizontalLineTo(13.723f)
                    arcTo(0.25f, 0.25f, 0.0f, false, false, 13.508f, 2.117f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF7f7f7f)),
                    fillAlpha = 0.0f, strokeAlpha = 0.496213f, strokeLineWidth = 0.0661458f,
                    strokeLineCap = Butt, strokeLineJoin = Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(12.667f, 3.175f)
                    verticalLineTo(13.725f)
                    horizontalLineTo(4.233f)
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF404040)),
                    fillAlpha = 0.0f, strokeAlpha = 0.50362f, strokeLineWidth = 0.132292f,
                    strokeLineCap = Butt, strokeLineJoin = Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(4.299f, 13.758f)
                    verticalLineTo(3.241f)
                    horizontalLineToRelative(8.401f)
                }
                path(
                    fill = SolidColor(Color(0xFFababab)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Square, strokeLineJoin = Round,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveToRelative(4.759f, 3.44f)
                    curveToRelative(-0.145f, 0.0f, -0.261f, 0.117f, -0.261f, 0.261f)
                    verticalLineTo(4.762f)
                    horizontalLineTo(12.435f)
                    verticalLineTo(3.696f)
                    curveTo(12.432f, 3.553f, 12.317f, 3.44f, 12.174f, 3.44f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF8c33b3)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Square, strokeLineJoin = Round,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveToRelative(4.498f, 11.642f)
                    verticalLineToRelative(1.591f)
                    curveToRelative(0.0f, 0.145f, 0.117f, 0.261f, 0.261f, 0.261f)
                    horizontalLineToRelative(7.415f)
                    curveToRelative(0.145f, 0.0f, 0.261f, -0.117f, 0.261f, -0.261f)
                    verticalLineToRelative(-1.591f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFdcdcdc)), stroke = SolidColor(Color(0x00000000)),
                    strokeAlpha = 0.50362f, strokeLineWidth = 0.080847f, strokeLineCap = Butt,
                    strokeLineJoin = Round, strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(4.498f, 4.762f)
                    horizontalLineToRelative(7.937f)
                    verticalLineToRelative(6.879f)
                    horizontalLineToRelative(-7.937f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFFffffff)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.026f, strokeLineCap = Square, strokeLineJoin = Round,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveToRelative(6.388f, 11.925f)
                    lineToRelative(-0.466f, 1.305f)
                    lineToRelative(-0.164f, 0.0f)
                    lineToRelative(-0.466f, -1.305f)
                    lineToRelative(0.172f, 0.0f)
                    lineToRelative(0.294f, 0.837f)
                    quadToRelative(0.029f, 0.08f, 0.049f, 0.152f)
                    quadToRelative(0.02f, 0.069f, 0.033f, 0.133f)
                    quadToRelative(0.013f, -0.064f, 0.033f, -0.135f)
                    quadToRelative(0.02f, -0.071f, 0.049f, -0.153f)
                    lineToRelative(0.292f, -0.833f)
                    close()
                    moveTo(7.799f, 12.54f)
                    lineToRelative(0.451f, 0.0f)
                    lineToRelative(0.0f, 0.64f)
                    quadToRelative(-0.106f, 0.035f, -0.214f, 0.051f)
                    quadToRelative(-0.108f, 0.016f, -0.245f, 0.016f)
                    quadToRelative(-0.203f, 0.0f, -0.342f, -0.08f)
                    quadToRelative(-0.139f, -0.082f, -0.212f, -0.232f)
                    quadToRelative(-0.071f, -0.152f, -0.071f, -0.358f)
                    quadToRelative(0.0f, -0.205f, 0.08f, -0.354f)
                    quadToRelative(0.08f, -0.15f, 0.23f, -0.232f)
                    quadToRelative(0.152f, -0.084f, 0.365f, -0.084f)
                    quadToRelative(0.11f, 0.0f, 0.206f, 0.02f)
                    quadToRelative(0.099f, 0.02f, 0.183f, 0.057f)
                    lineToRelative(-0.062f, 0.143f)
                    quadToRelative(-0.069f, -0.031f, -0.157f, -0.053f)
                    quadToRelative(-0.086f, -0.022f, -0.179f, -0.022f)
                    quadToRelative(-0.234f, 0.0f, -0.365f, 0.141f)
                    quadToRelative(-0.13f, 0.141f, -0.13f, 0.386f)
                    quadToRelative(0.0f, 0.155f, 0.049f, 0.276f)
                    quadToRelative(0.051f, 0.119f, 0.159f, 0.186f)
                    quadToRelative(0.108f, 0.066f, 0.283f, 0.066f)
                    quadToRelative(0.086f, 0.0f, 0.146f, -0.009f)
                    quadToRelative(0.06f, -0.009f, 0.11f, -0.022f)
                    lineToRelative(0.0f, -0.387f)
                    lineToRelative(-0.287f, 0.0f)
                    close()
                    moveTo(9.248f, 13.229f)
                    lineToRelative(0.0f, -1.305f)
                    lineToRelative(0.164f, 0.0f)
                    lineToRelative(0.0f, 1.158f)
                    lineToRelative(0.57f, 0.0f)
                    lineToRelative(0.0f, 0.146f)
                    close()
                    moveTo(11.63f, 12.88f)
                    quadToRelative(0.0f, 0.174f, -0.126f, 0.27f)
                    quadToRelative(-0.126f, 0.097f, -0.34f, 0.097f)
                    quadToRelative(-0.11f, 0.0f, -0.203f, -0.016f)
                    quadToRelative(-0.093f, -0.016f, -0.155f, -0.046f)
                    lineToRelative(0.0f, -0.157f)
                    quadToRelative(0.066f, 0.029f, 0.163f, 0.053f)
                    quadToRelative(0.099f, 0.024f, 0.203f, 0.024f)
                    quadToRelative(0.146f, 0.0f, 0.219f, -0.057f)
                    quadToRelative(0.075f, -0.057f, 0.075f, -0.153f)
                    quadToRelative(0.0f, -0.064f, -0.027f, -0.108f)
                    quadToRelative(-0.027f, -0.044f, -0.095f, -0.08f)
                    quadToRelative(-0.066f, -0.038f, -0.185f, -0.08f)
                    quadToRelative(-0.166f, -0.06f, -0.252f, -0.148f)
                    quadToRelative(-0.084f, -0.088f, -0.084f, -0.239f)
                    quadToRelative(0.0f, -0.104f, 0.053f, -0.177f)
                    quadToRelative(0.053f, -0.075f, 0.146f, -0.115f)
                    quadToRelative(0.095f, -0.04f, 0.217f, -0.04f)
                    quadToRelative(0.108f, 0.0f, 0.197f, 0.02f)
                    quadToRelative(0.09f, 0.02f, 0.163f, 0.053f)
                    lineToRelative(-0.051f, 0.141f)
                    quadToRelative(-0.068f, -0.029f, -0.148f, -0.049f)
                    quadToRelative(-0.079f, -0.02f, -0.164f, -0.02f)
                    quadToRelative(-0.122f, 0.0f, -0.185f, 0.053f)
                    quadToRelative(-0.062f, 0.051f, -0.062f, 0.137f)
                    quadToRelative(0.0f, 0.066f, 0.027f, 0.11f)
                    quadToRelative(0.027f, 0.044f, 0.09f, 0.079f)
                    quadToRelative(0.062f, 0.035f, 0.168f, 0.075f)
                    quadToRelative(0.115f, 0.042f, 0.194f, 0.091f)
                    quadToRelative(0.08f, 0.048f, 0.121f, 0.115f)
                    quadToRelative(0.042f, 0.068f, 0.042f, 0.17f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(5.022f, 9.758f)
                    lineTo(8.244f, 9.41f)
                    lineTo(8.244f, 9.618f)
                    lineTo(5.022f, 9.955f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(5.022f, 9.439f)
                    lineTo(8.244f, 9.091f)
                    lineTo(8.244f, 9.3f)
                    lineTo(5.022f, 9.636f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(8.157f, 7.966f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(8.15f, 7.883f)
                    lineToRelative(0.094f, 0.0f)
                    lineToRelative(0.0f, 1.589f)
                    lineToRelative(-0.094f, 0.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(7.098f, 8.211f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(7.092f, 8.128f)
                    lineToRelative(0.094f, 0.0f)
                    lineToRelative(0.0f, 1.541f)
                    lineToRelative(-0.094f, 0.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(6.04f, 8.424f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(6.033f, 8.34f)
                    lineToRelative(0.094f, 0.0f)
                    lineToRelative(0.0f, 1.412f)
                    lineToRelative(-0.094f, 0.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(4.982f, 8.626f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(4.975f, 8.543f)
                    lineToRelative(0.094f, 0.0f)
                    lineToRelative(0.0f, 1.412f)
                    lineToRelative(-0.094f, 0.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveToRelative(12.164f, 7.337f)
                    lineToRelative(-1.147f, -0.033f)
                    lineToRelative(0.001f, 0.186f)
                    lineToRelative(1.15f, 0.027f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveToRelative(12.168f, 7.052f)
                    lineToRelative(-2.476f, -0.073f)
                    lineToRelative(0.0f, 0.203f)
                    lineToRelative(2.476f, 0.071f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(12.162f, 8.323f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(12.168f, 8.406f)
                    lineToRelative(-0.094f, -0.0f)
                    lineToRelative(-0.0f, -1.354f)
                    lineToRelative(0.094f, -0.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(11.103f, 8.486f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(11.11f, 8.57f)
                    lineToRelative(-0.092f, -0.0f)
                    lineToRelative(-0.0f, -1.526f)
                    lineToRelative(0.092f, -0.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(9.78f, 8.158f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, -0.565f, 0.32f)
                    arcToRelative(0.202f, 0.327f, 54.903f, true, false, 0.565f, -0.32f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Bevel,
                    strokeLineMiter = 4.0f, pathFillType = NonZero
                ) {
                    moveTo(9.787f, 8.242f)
                    lineToRelative(-0.094f, -0.0f)
                    lineToRelative(-0.0f, -1.161f)
                    lineToRelative(0.094f, -0.0f)
                    close()
                }
            }
        }
            .build()
        return _icLauncherForeground!!
    }

private var _icLauncherForeground: ImageVector? = null
