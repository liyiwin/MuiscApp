package com.example.musicapp.components

import android.graphics.BlurMaskFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
    color:陰影顏色,
    alpha:陰影顏色透明度,
    topLeftBorderRadius: 陰影左上角度,
    topRightBorderRadius:陰影右上角度,
    bottomLeftBorderRadius: 陰影左下角度,
    bottomRightBorderRadius: 陰影右下角度,
    shadowRadius:Dp = shadowLayer 模糊半徑，radius越大越模糊，越小越清晰，但是如果radius設置為0，則陰影消失不見
    offsetX:陰影Ｘ軸位移
    offsetY:陰影Ｙ軸位移
    */
@RequiresApi(Build.VERSION_CODES.O)
fun Modifier.drawDropShadow(
    color: Color,
    alpha:Float = 0.2f,
    topLeftBorderRadius: Dp =  0.dp,
    topRightBorderRadius: Dp =  0.dp,
    bottomLeftBorderRadius: Dp =  0.dp,
    bottomRightBorderRadius: Dp =  0.dp,
    shadowRadius: Dp = 20.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
) = this.drawBehind{
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = .0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha =  alpha).value.toLong())
    this.drawIntoCanvas {
        val paint = Paint()
        val frameWorkPaint = paint.asFrameworkPaint()
        frameWorkPaint.color = transparentColor
        frameWorkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        val path = Path()
        val rect =
            Rect(0.dp.toPx(),0.dp.toPx(), (this.size.width).toDp().toPx(),(this.size.height).toDp().toPx())
        path.addRoundRect(
            RoundRect(rect,
            topLeft   =    CornerRadius( topLeftBorderRadius.toPx(),  topLeftBorderRadius.toPx()),
            topRight = CornerRadius(topRightBorderRadius.toPx(),  topRightBorderRadius.toPx()),
            bottomLeft = CornerRadius(bottomLeftBorderRadius.toPx(),  bottomLeftBorderRadius.toPx()),
            bottomRight = CornerRadius(bottomRightBorderRadius.toPx(),  bottomRightBorderRadius.toPx())
            )
        )
        it.drawPath(path,paint)
    }
}



/*
    color:陰影底色,
    alpha:陰影底色透明度,
    shadowAlpha:陰影模糊顏色透明度,
    left: 左邊陰影寬度,
    top:上面陰影寬度,
    right:右邊陰影寬度,
    bottom:下面陰影寬度,
    blur:blurMaskFilter模糊半徑,
    topLeftBorderRadius: 陰影底色左上角度,
    topRightBorderRadius:陰影底色右上角度,
    bottomLeftBorderRadius: 陰影底色左下角度,
    bottomRightBorderRadius: 陰影底色右下角度,
    shadowRadius:Dp = shadowLayer 模糊半徑，radius越大越模糊，越小越清晰，但是如果radius設置為0，則陰影消失不見*/

@RequiresApi(Build.VERSION_CODES.O)
fun Modifier.drawInnerShadow(
    color: Color,
    alpha: Float = 0.2f,
    shadowAlpha:Float = 0.9f,
    left:Float = 20F,
    top:Float   = 20F,
    right:Float   = 20F,
    bottom:Float = 20F,
    blur:Float = 100F,
    topLeftBorderRadius: Dp =  0.dp,
    topRightBorderRadius: Dp =  0.dp,
    bottomLeftBorderRadius: Dp =  0.dp,
    bottomRightBorderRadius: Dp =  0.dp,
    shadowRadius: Dp = 20.dp,
)= this.drawBehind{
    val backgroundColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    val shadowTransparentColor = android.graphics.Color.toArgb(Color.White.copy(alpha = .0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(Color.White.copy(alpha =  shadowAlpha).value.toLong())
    this.drawIntoCanvas {
        val shadowPaint = Paint()
        val backgroundPaint = Paint()
        val shadowFrameWorkPaint = shadowPaint.asFrameworkPaint()
        val backgroundFrameWorkPaint = backgroundPaint.asFrameworkPaint()

        shadowFrameWorkPaint.setMaskFilter(BlurMaskFilter(blur, BlurMaskFilter.Blur.INNER));
        shadowFrameWorkPaint.color = shadowTransparentColor
        shadowFrameWorkPaint.setShadowLayer(
            shadowRadius.toPx(),
            0.dp.toPx(),
            0.dp.toPx(),
            shadowColor
        )
        backgroundFrameWorkPaint.setMaskFilter(BlurMaskFilter(blur, BlurMaskFilter.Blur.INNER));
        backgroundFrameWorkPaint.setColor(backgroundColor)

        val path = Path()
        val rect =
            Rect(-left.toDp().toPx(),-top.toDp().toPx(), ((this.size.width+right).toDp()).toPx(),((this.size.height+bottom).toDp()).toPx())
        path.addRoundRect(
            RoundRect(rect,
            topLeft   =    CornerRadius( topLeftBorderRadius.toPx(),  topLeftBorderRadius.toPx()),
            topRight = CornerRadius(topRightBorderRadius.toPx(),  topRightBorderRadius.toPx()),
            bottomLeft = CornerRadius(bottomLeftBorderRadius.toPx(),  topRightBorderRadius.toPx()),
            bottomRight = CornerRadius(bottomRightBorderRadius.toPx(),  topRightBorderRadius.toPx())
            )
        )
        it.drawPath(path,backgroundPaint)
        it.drawPath(path, shadowPaint)
    }
}