package com.example.musicapp.viewcomponent.bottomnavigation

import android.graphics.Point

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BottomNavigationBar(color: Color,selectedIndex:Int, modifier: Modifier, items:List<BottomNavigationItem>, selectedListener: (BottomNavigationItem)->Unit) {
    val animalCenterIndex = remember { mutableStateOf(selectedIndex) }
    val animalBoolean = remember { mutableStateOf(true) }
    val animalBooleanState: Float by animateFloatAsState( if (animalBoolean.value) 0f else 1f,  animationSpec = TweenSpec(durationMillis = 600) )
    val indexValue: Float by animateFloatAsState( animalCenterIndex.value.toFloat(), animationSpec = TweenSpec(durationMillis = 500))

// height(100.dp) + clickable {  } 是為了 防止誤點bottomNavigaition 以外的畫面
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            . clickable {  },
        contentAlignment = Alignment.BottomEnd,
    ) {
        Canvas(modifier = modifier
            .fillMaxWidth()
            .height(70.dp), onDraw = {
            drawShadow( this, indexValue)
            drawBackground(this,indexValue,color)
        })
        Row(
            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach {
                Image(
                    painter =  painterResource(id = it.unSelectedRes),
                    contentDescription = stringResource(id = it.descriptionResourceId),
                    modifier = modifier(animalCenterIndex, it.index, animalBooleanState)
                        .clickable {
                            animalBoolean.value = !animalBoolean.value
                            animalCenterIndex.value = it.index
                            selectedListener.invoke(it)
                        }
                )
            }
        }
        Row(
            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ){
            items.forEach{
                Text(
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    text = stringResource(id = it.descriptionResourceId),
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(start = 2.dp,top =10.dp,bottom = 5.dp).width(50.dp)
                )
            }

        }
    }

}

fun drawShadow( drawScope: DrawScope, indexValue: Float){
    drawScope.apply {

        drawIntoCanvas {
            val paint = buildShadowPaint(this)
            val singleItemWidth = getSingleTabWidth(size)
            val selectedItemCenterXCoordinate = getSelectedItemCenterXCoordinate(size,indexValue)
            val path = getPath(singleItemWidth,selectedItemCenterXCoordinate,size)
           it.save()
            it.drawCircle(Offset(selectedItemCenterXCoordinate, singleItemWidth/6), singleItemWidth/3, paint)
            it.drawPath(path,paint)
        }

    }

}

fun drawBackground( drawScope: DrawScope,indexValue: Float,backgroundColor: Color){
    // the coordinates of the first curve
    drawScope.apply{
        drawIntoCanvas{
            val paint =buildBackgroundPaint(backgroundColor)
            val singleItemWidth = getSingleTabWidth(size)
            val selectedItemCenterXCoordinate = getSelectedItemCenterXCoordinate(size,indexValue)
            val path = getPath(singleItemWidth,selectedItemCenterXCoordinate,size)
            it.save()
            it.drawCircle(Offset(selectedItemCenterXCoordinate, singleItemWidth/6), singleItemWidth/3, paint)
            it.clipPath(path)
            it.nativeCanvas.drawColor(backgroundColor.toArgb())
        }
    }
}

fun buildBackgroundPaint(backgroundColor:Color):Paint{
    val paint = Paint()
    paint.color = backgroundColor
    paint.style = PaintingStyle.Fill
    return paint
}

fun buildShadowPaint(drawScope: DrawScope):Paint{
    val paint = Paint()
    drawScope.apply {
        val alpha = 0.7f
        val shadowRadius =2.dp
        val offsetX = 2.dp
        val offsetY = -3.dp
        val color = Color.DarkGray
        val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = .0f).value.toLong())
        val shadowColor = android.graphics.Color.toArgb(color.copy(alpha =  alpha).value.toLong())
        val frameWorkPaint = paint.asFrameworkPaint()
        frameWorkPaint.color = transparentColor
        frameWorkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
    }
    return  paint
}

fun getPath(singleItemWidth:Float, selectedItemCenterXCoordinate:Float, size: Size):Path{
    val path = Path()
    val CURVE_CIRCLE_RADIUS =(singleItemWidth/2.3).toInt()

    var mFirstCurveStartPoint = Point();
    var mFirstCurveEndPoint = Point();
    var mFirstCurveControlPoint1 = Point();
    var mFirstCurveControlPoint2 = Point();

    var mSecondCurveStartPoint = Point();
    var mSecondCurveEndPoint = Point();
    var mSecondCurveControlPoint1 = Point();
    var  mSecondCurveControlPoint2 = Point();

    // the coordinates (x,y) of the start point before curve
    mFirstCurveStartPoint.set(selectedItemCenterXCoordinate.toInt()- (CURVE_CIRCLE_RADIUS * 2) - (CURVE_CIRCLE_RADIUS / 3), 0);
    // the coordinates (x,y) of the end point after curve
    mFirstCurveEndPoint.set(selectedItemCenterXCoordinate.toInt(), CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4));
    // same thing for the second curve
    mSecondCurveStartPoint = mFirstCurveEndPoint;
    mSecondCurveEndPoint.set(selectedItemCenterXCoordinate.toInt() + (CURVE_CIRCLE_RADIUS * 2) + (CURVE_CIRCLE_RADIUS / 3), 0);

    // the coordinates (x,y)  of the 1st control point on a cubic curve
    mFirstCurveControlPoint1.set(mFirstCurveStartPoint.x + CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4), mFirstCurveStartPoint.y);
    // the coordinates (x,y)  of the 2nd control point on a cubic curve
    mFirstCurveControlPoint2.set(mFirstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS * 2) + CURVE_CIRCLE_RADIUS, mFirstCurveEndPoint.y);

    mSecondCurveControlPoint1.set(mSecondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS, mSecondCurveStartPoint.y);
    mSecondCurveControlPoint2.set(mSecondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4)), mSecondCurveEndPoint.y);

    path.moveTo(0f, 0f);
    path.lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat());

    path.cubicTo(
        mFirstCurveControlPoint1.x.toFloat(), mFirstCurveControlPoint1.y.toFloat(),
        mFirstCurveControlPoint2.x.toFloat(), mFirstCurveControlPoint2.y.toFloat(),
        mFirstCurveEndPoint.x.toFloat(), mFirstCurveEndPoint.y.toFloat()
    );

    path.cubicTo(
        mSecondCurveControlPoint1.x.toFloat(), mSecondCurveControlPoint1.y.toFloat(),
        mSecondCurveControlPoint2.x.toFloat(), mSecondCurveControlPoint2.y.toFloat(),
        mSecondCurveEndPoint.x.toFloat(), mSecondCurveEndPoint.y.toFloat()
    );

    path.lineTo(size.width, 0f);
    path.lineTo(size.width, size.height);
    path.lineTo(0f, size.height);

    return path
}



fun getSingleTabWidth(size:Size):Float{
    return size.width / 5
}

fun getSelectedItemCenterXCoordinate(size:Size,selectedIndex:Float):Float{
    val singleItemWidth = size.width / 5
    val selectedItemCenterXCoordinate = singleItemWidth / 2+singleItemWidth * selectedIndex
    return selectedItemCenterXCoordinate
}


fun modifier(
    animalCenterIndex: MutableState<Int>,
    i: Int,
    animalBooleanState: Float
): Modifier {
    return if (animalCenterIndex.value == i) {
        return Modifier
            .padding(bottom = 45.dp)
            .width(25.dp)
            .height(25.dp)
            .rotate(animalBooleanState * 360)
    } else {
        return Modifier
            .padding(top = 15.dp)
            .width(25.dp)
            .height(25.dp)
    }

}