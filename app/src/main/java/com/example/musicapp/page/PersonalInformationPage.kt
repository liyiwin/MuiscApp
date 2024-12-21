package com.example.musicapp.page

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.example.kkbox_music_app.components.dialog.AlarmDialog
import com.example.kkbox_music_app.components.dialog.FailureDialog
import com.example.kkbox_music_app.components.dialog.LoadingDialog
import com.example.kkbox_music_app.components.dialog.SuccessDialog
import com.example.musicapp.R
import com.example.musicapp.bean.remote.PersonalInformation
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.PersonalInformationViewModel
import com.example.musicapp.viewmodel.TrackDetailViewModel
import com.example.musicapp.viewmodel.state.RequestState

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalInformationPage (viewModel: PersonalInformationViewModel, onBackPress:()->Unit, navigationController:(screen: Screen)->Unit,cleanAppDataController:()->Unit){


    RequestStateDialog(viewModel)

    val isLogoutClicked  = remember { mutableStateOf(false) }

     if(isLogoutClicked.value) {
         logoutAlarm(
             onConfirm = {
                 cleanAppDataController.invoke()
                 navigationController.invoke(Screen.OauthScreen())
                 isLogoutClicked.value = false
             },
             onCancel = {
                 isLogoutClicked.value = false
             }
         )
     }


    if (viewModel.getInformation().value == null){
        viewModel.fetchInformation()
    }

    Box(
         modifier = Modifier
             .background(Color("#EEEEEE".toColorInt()))
             .fillMaxSize()
             .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
     ) {
         PersonalInformationBackground()
         PersonalInformationContainer(viewModel,onBackPress,isLogoutClicked)
    }

}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  logoutAlarm(onConfirm:()->Unit,onCancel:()->Unit){
    AlarmDialog(
        "提醒",
        "確認是否登出，登出後將會清楚所有資料！ ",
        modifier = Modifier,
        onConfirm,
        onCancel
    )
}


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestStateDialog(viewModel: PersonalInformationViewModel){
    val requestState = viewModel.getLatestRequestState().observeAsState()
    requestState.value?.let {
        when(it){

            is RequestState.Loading ->{
                LoadingDialog(modifier = Modifier)
            }

            is RequestState.Success ->{
                SuccessDialog(title = "", message = it.message, modifier = Modifier) {
                    viewModel.cleanLatestRequestState()
                }
            }

            is RequestState.Failure ->{
                FailureDialog(title = "加載失敗", message =it.message, modifier = Modifier){
                    viewModel.cleanLatestRequestState()
                }
            }

            is RequestState.None->{

            }

        }
    }
}


@Composable
fun  PersonalInformationBackground(){
    Card(
        shape  = RoundedCornerShape(25.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp, bottom = 50.dp, start = 10.dp, end = 10.dp)

    ){

    }
}

@Composable
fun PersonalInformationContainer(viewModel: PersonalInformationViewModel, onBackPress:()->Unit,isLogoutClicked:MutableState<Boolean>) {

    val personalInformation = viewModel.getInformation().observeAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PersonalInformationPageTitle(onBackPress)
        if( personalInformation.value == null) PersonalInformationDefaultContent()
        else PersonalInformationContent(personalInformation.value!!)
       LogoutContent (isLogoutClicked)
    }
}


@Composable
fun PersonalInformationPageTitle(onBackPress:()->Unit){
    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable {
                onBackPress.invoke()
            }
    ){
        Icon(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterStart),
            contentDescription = "",
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_black)
        )
    }
}

@Composable
fun ColumnScope.PersonalInformationDefaultContent(){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(200.dp))
            .size(200.dp)
            .align(Alignment.CenterHorizontally)
            .background(Color.White)
    ){}

    Text(text = "-------",color = Color.Black, fontSize = 25.sp, modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(10.dp))
    Text(text = "-------",color = Color.Gray, fontSize = 15.sp,modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(10.dp))
    Text(text = "-------",color = Color.Black, fontSize = 20.sp, modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(50.dp))

}

@Composable
fun ColumnScope.PersonalInformationContent(personalInformation: PersonalInformation){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(200.dp))
            .size(200.dp)
            .align(Alignment.CenterHorizontally)
            .background(Color.White)
    ){
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(200.dp))
                .size(200.dp),
            painter = rememberAsyncImagePainter(personalInformation.images[1].url),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }

    val name = personalInformation.name;
    val id = personalInformation.id;
    var description = "無自我介紹"
    if(personalInformation.description.isNotEmpty()){
        description =personalInformation.description
    }
    Text(text = (name),color = Color.Black, fontSize = 25.sp, modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(10.dp))
    Text(text = ("Id:$id"),color = Color.Gray, fontSize = 15.sp,modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(5.dp))
    Text(text = (description),color = Color.Black, fontSize = 20.sp, modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(50.dp))
}

@Composable
fun ColumnScope.LogoutContent ( isLogoutClicked:MutableState<Boolean>){
    Card (
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color("#F6F6F6".toColorInt()),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)

            .drawDropShadow(
                Color.DarkGray,
                0.5f,
                topLeftBorderRadius = 10.dp,
                topRightBorderRadius = 10.dp,
                bottomLeftBorderRadius = 10.dp,
                bottomRightBorderRadius = 10.dp,
                shadowRadius = 2.dp,
                offsetX = 2.dp,
                offsetY = 2.dp,
            )
            .drawDropShadow(
                Color("#FDFDFD".toColorInt()),
                0.7f,
                topLeftBorderRadius = 10.dp,
                topRightBorderRadius = 10.dp,
                bottomLeftBorderRadius = 10.dp,
                bottomRightBorderRadius = 10.dp,
                shadowRadius = 2.dp,
                offsetX = -5.dp,
                offsetY = 0.dp,
            )
            .clickable {
                isLogoutClicked.value = true
            }
    ){

        Row{
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically),
                contentDescription = "",
                imageVector = ImageVector.vectorResource(id = R.drawable.logout),
                tint = Color.Black
            )
            Text(text = "登出",color = Color.Black, fontSize = 17.sp, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp))
        }

    }
}