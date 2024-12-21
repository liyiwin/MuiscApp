package com.example.musicapp.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier,onDragStart:() ->Unit,onDragging:(progress:Float) ->Unit,onDragEnd:(progress:Float) ->Unit) {
    val progressWidth = remember { mutableStateOf(0f) } // 記錄進度條總寬度
    val isShowProgressIndicator = remember{mutableStateOf(false)}
   Canvas(modifier = modifier
        .height(3.dp)
        .fillMaxWidth()
        .drawDropShadow(
            color = Color.Gray,
            0.9f,
            offsetY = 5.dp,
            shadowRadius = 3.dp,
        )
        .pointerInput(Unit) {
            awaitPointerEventScope {
                var isDragging = false
                var  newProgress = 0f
                while (true){
                     val event = awaitPointerEvent()
                     val changes = event.changes
                     val down = changes.firstOrNull()
                     if (down?.pressed == true){
                           if(!isDragging){
                               onDragStart.invoke()
                               isDragging = true
                           }
                           isShowProgressIndicator.value = true
                          newProgress = (down.position.x  / progressWidth.value).coerceIn(0f, 1f)
                          onDragging(newProgress)
                    }else{
                           if(isDragging){
                               onDragEnd.invoke(newProgress)
                               isDragging = false
                           }
                           isShowProgressIndicator.value = false
                    }
               }
            }
          }

        .onGloballyPositioned { layoutCoordinates ->
            progressWidth.value = layoutCoordinates.size.width.toFloat()
        }
    ) {

        // 背景條
        drawRoundRect(
            color = Color.White,
            size = size,
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        // 前景條
        drawRoundRect(
            color = Color.Gray,
            size = size.copy(width = size.width * progress),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        if(isShowProgressIndicator.value){
            // 圓點滑塊
            drawCircle(
                color = Color.Gray,
                radius = 5.dp.toPx(),
                center = Offset(size.width * progress, size.height / 2)
            )
        }

    }
}

