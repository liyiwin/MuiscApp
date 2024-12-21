package com.example.musicapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R

@Composable
fun  CheckBox (sideLength:Dp,isChecked:Boolean,onCheckChange:(Boolean)->Unit){


    Surface(
        color = Color("#F9F9F9".toColorInt()),
        modifier = Modifier

            .drawInnerShadow(
                color = Color("#F4F4F4".toColorInt()),
                1f,
                left = 0F,
                top = 0F,
                right =  (sideLength.value/5),
                bottom =  (sideLength.value/5),
                blur = 1F,
                shadowRadius =3.dp,
            )
            .drawInnerShadow(
                color = Color("#B6B6B6".toColorInt()),
                1f,
                left =  (sideLength.value/5),
                top =  (sideLength.value/5),
                right = 0F,
                bottom = 0F,
                blur = 1F,
                shadowRadius =3.dp,
            )

            .clickable {
                onCheckChange( !isChecked)
            }
            .size(sideLength, sideLength)
            .padding(sideLength/5)
    ){
        if(isChecked){
            Icon(
                modifier = Modifier.size(sideLength*3/5,sideLength*3/5) ,
                contentDescription = "",
                imageVector = ImageVector.vectorResource(id = R.drawable.tick),
                tint = Color.Red,

            )
        }

    }

}