package com.example.kkbox_music_app.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.musicapp.R
import com.example.musicapp.components.drawDropShadow

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RemindDialog(title:String, message:String, modifier: Modifier,onConfirm:()->Unit){

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        RemindDialogUI(title, message, modifier, onConfirm)
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RemindDialogUI(title:String, message:String, modifier: Modifier,onConfirm:()->Unit){
    Box(
        modifier = modifier.size(350.dp),
        contentAlignment = Alignment.Center
    ){
        val mainSIze = 220.dp
        val iconSize = 70.dp
        Card (
            shape = RoundedCornerShape(18.dp),
            backgroundColor = Color(0xFFE6E6E6),
            modifier = modifier.size(mainSIze)

        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = iconSize/2+20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = title,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier
                        .padding(top =10.dp),
                    text = message,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Card (
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color(0xFF00CCFF),
                    modifier = modifier
                        .padding(top =20.dp)
                        .drawDropShadow(
                            Color.DarkGray,
                            0.7f,
                            topLeftBorderRadius =20.dp,
                            topRightBorderRadius = 20.dp,
                            bottomLeftBorderRadius = 20.dp,
                            bottomRightBorderRadius = 20.dp,
                            shadowRadius = 2.dp,
                            offsetX = 5.dp,
                            offsetY = 2.dp,
                        )
                        .drawDropShadow(
                            Color.White,
                            0.9f,
                            topLeftBorderRadius = 20.dp,
                            topRightBorderRadius = 20.dp,
                            bottomLeftBorderRadius = 20.dp,
                            bottomRightBorderRadius = 20.dp,
                            shadowRadius = 3.dp,
                            offsetX = -6.dp,
                            offsetY = -0.dp,
                        ),
                    onClick = {
                        onConfirm.invoke()
                    }
                ){
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "確認",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Box(
            modifier = modifier
                .offset(x = 0.dp, y = -((mainSIze)/ 2))
                .size(iconSize),
            contentAlignment = Alignment.Center
        ){
            Card (
                shape = RoundedCornerShape(100.dp),
                backgroundColor = Color(0xFF00CCFF),
                modifier = modifier.size(iconSize)
                    .drawDropShadow(
                        Color.DarkGray,
                        0.7f,
                        topLeftBorderRadius = 100.dp,
                        topRightBorderRadius = 100.dp,
                        bottomLeftBorderRadius = 100.dp,
                        bottomRightBorderRadius = 100.dp,
                        shadowRadius = 2.dp,
                        offsetX = 5.dp,
                        offsetY = 2.dp,
                    )
                    .drawDropShadow(
                        Color.White,
                        0.9f,
                        topLeftBorderRadius = 100.dp,
                        topRightBorderRadius = 100.dp,
                        bottomLeftBorderRadius = 100.dp,
                        bottomRightBorderRadius = 100.dp,
                        shadowRadius = 3.dp,
                        offsetX = -6.dp,
                        offsetY = -0.dp,
                    ),
            ){
                Icon(
                    modifier = modifier.size(40.dp).padding(20.dp),
                    contentDescription = "",
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_remind_white),
                    tint = Color.White
                )
            }
        }

    }
}

