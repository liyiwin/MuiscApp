package com.example.kkbox_music_app.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
fun  InputDialog(title:String, modifier: Modifier, onConfirm:(String)->Unit){
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ){
        InputDialogUI(title, modifier, onConfirm)
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  InputDialogUI(title:String, modifier: Modifier, onConfirm:(String)->Unit){

    var textValue by remember { mutableStateOf(TextFieldValue("")) }

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
,
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

                TextField(
                    value = textValue,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFE6E6E6),
                    ),
                    onValueChange = { newText ->
                        textValue = newText
                    }
                )
                Card (
                    shape = RoundedCornerShape(20.dp),
//                    backgroundColor = Color(0xFF00CCFF),
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
                        onConfirm.invoke(textValue.text)
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
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_promote_white),
                    tint = Color.White
                )
            }
        }

    }
}

