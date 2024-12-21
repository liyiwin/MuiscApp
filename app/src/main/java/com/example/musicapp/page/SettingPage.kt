package com.example.musicapp.page

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.routing.Screen
import com.example.musicapp.tool.information.AppInformationUtil
import me.nikhilchaudhari.library.BuildConfig

@Composable
fun SettingPage(navigationController:(screen: Screen)->Unit){
    Column(
        modifier = Modifier
            .background(Color("#EBE8E8".toColorInt()))
            .fillMaxSize()
            .padding(15.dp)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){
        title()
        personalInformation(informationSelected = {
            navigationController.invoke(Screen.PersonalInformationScreen())
        })
        otherFunctions(functionSelectedListener = {
             if(it == 0)navigationController.invoke(Screen.FavoriteTracksScreen())
        })
        appInformation()
    }
}

@Composable
fun title(){
    Text(
        fontSize = 25.sp,
        text = "個人頁面",
        textAlign = TextAlign.Start,
        color = Color.Black,
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    )

}

@Composable
fun personalInformation(informationSelected:()->Unit){
    Column (
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
            .clickable {
                informationSelected.invoke()
            },
    ){
        Text(
            fontSize = 18.sp,
            text = "個人資訊 ",
            textAlign = TextAlign.Start,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
        )
        Card(
            elevation = 0.dp,
            shape  = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min) ,
        ){
            Row(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_personal_information),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp).weight(1f),
                    alignment = Alignment.CenterStart
                )
                Text(
                    fontSize = 18.sp,
                    text = "帳號",
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically).weight(3f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_right_arrow),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    alignment = Alignment.CenterEnd
                )
            }
        }

    }

}

@Composable
fun otherFunctions(functionSelectedListener:(Int)->Unit){
    Column (
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ){
        Text(
            fontSize = 18.sp,
            text = "其他功能 ",
            textAlign = TextAlign.Start,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
        )
        Card(
            elevation = 0.dp,
            shape  = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                . clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                         functionSelectedListener.invoke(0)
                    }
                )
            ,
        ){
            Row(
                modifier = Modifier
                    .padding(25.dp)
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
            ){
                Text(
                    fontSize = 18.sp,
                    text = "收藏歌曲",
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically).weight(4f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_right_arrow),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp).align(Alignment.CenterVertically).weight(1f),
                    alignment = Alignment.CenterEnd
                )
            }
        }

    }

}

@Composable
fun appInformation(){
    Column (
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ){
        Text(
            fontSize = 18.sp,
            text = "版本資訊 ",
            textAlign = TextAlign.Start,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
        )
        Card(
            elevation = 0.dp,
            shape  = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min) ,
        ){
            Row(
                modifier = Modifier
                    .padding(25.dp)
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
            ){
                Text(
                    fontSize = 18.sp,
                    text = "版本號：v"+AppInformationUtil.getAppVersion(LocalContext.current),
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }


    }
}