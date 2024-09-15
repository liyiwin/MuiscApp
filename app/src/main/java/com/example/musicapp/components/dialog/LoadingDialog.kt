package com.example.kkbox_music_app.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.musicapp.R

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  LoadingDialog( modifier: Modifier){
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ){
        LoadingDialogUI(modifier)
    }
}


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  LoadingDialogUI( modifier: Modifier){
    Box(
        modifier = modifier.size(350.dp),
        contentAlignment = Alignment.Center
    ){
        val mainSIze = 220.dp
        Card (
            shape = RoundedCornerShape(18.dp),
            backgroundColor = Color(0xFFFFFFFF),
            modifier = modifier.size(mainSIze)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top =mainSIze/4),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.loading_img),
                    contentDescription = "",
                    modifier = Modifier
                        .size(70.dp)

                )

                Text(
                    modifier = Modifier
                        .padding(top =10.dp),
                    text = "資料加載中",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }


    }
}