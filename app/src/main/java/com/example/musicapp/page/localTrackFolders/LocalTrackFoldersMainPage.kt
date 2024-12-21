package com.example.musicapp.page.localTrackFolders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.kkbox_music_app.components.dialog.InputDialog
import com.example.musicapp.R
import com.example.musicapp.components.GridView
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.LocalTrackFoldersVIewModel
import java.io.File

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocalTrackFoldersMainPage(vIewModel: LocalTrackFoldersVIewModel, isRemoveMode: MutableState<Boolean>,navigationController:(screen: Screen)->Unit) {
    val showInputDialog = remember { mutableStateOf(false) }
    val list =vIewModel.getTotalPlayLists().observeAsState()
    Column(
        modifier = Modifier
            .background(Color("#F4F4F4".toColorInt()))
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){
        LocalTrackFoldersMainPageTopBar(
            onAddClicked = {
            showInputDialog.value = true
        }, onRemoveClicked = {
                isRemoveMode.value = true
        }
        )
        LocalTrackFoldersMainPageContainer(list,onSelectedListener = { file ->
            RouterDataStorage.localPlayList  = file
            navigationController.invoke(Screen.LocalTrackFolderDetailPage())
         })
        LocalTrackFolderNameInputDialog(
            showInputDialog,
            onAddPlayList = {
                vIewModel.addPlayList(it)
                vIewModel.updateTotalPlayLists()
                showInputDialog.value = false
            },
            onCancelAddPlayList = {
                showInputDialog.value = false
            }
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocalTrackFolderNameInputDialog(showInputDialog:MutableState<Boolean>, onAddPlayList:(String)->Unit, onCancelAddPlayList:()->Unit){
    if(showInputDialog.value){
        InputDialog(title = "輸入新資料夾名稱", modifier = Modifier
            , onConfirm ={
              onAddPlayList.invoke(it)
               },
            onCancel = {
                 onCancelAddPlayList.invoke()
            }
        )
    }
}

@Composable
fun LocalTrackFoldersMainPageTopBar(onAddClicked:()->Unit, onRemoveClicked:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp,Alignment.End)
    ){
        val iconSize = 60.dp
        Card (
            shape = RoundedCornerShape(30.dp),
            backgroundColor = Color("#F6F6F6".toColorInt()),
            modifier = Modifier
                .size(iconSize)

                .drawDropShadow(
                    Color.DarkGray,
                    0.7f,
                    topLeftBorderRadius = 100.dp,
                    topRightBorderRadius = 100.dp,
                    bottomLeftBorderRadius = 100.dp,
                    bottomRightBorderRadius = 100.dp,
                    shadowRadius = 2.dp,
                    offsetX = 2.dp,
                    offsetY = 2.dp,
                )
                .drawDropShadow(
                    Color("#FDFDFD".toColorInt()),
                    0.7f,
                    topLeftBorderRadius = 100.dp,
                    topRightBorderRadius = 100.dp,
                    bottomLeftBorderRadius = 100.dp,
                    bottomRightBorderRadius = 100.dp,
                    shadowRadius = 2.dp,
                    offsetX = -5.dp,
                    offsetY = 0.dp,
                )
                .clickable {
                    onAddClicked.invoke()
                }
        ){
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(17.dp),
                contentDescription = "",
                imageVector = ImageVector.vectorResource(id = R.drawable.add),
                tint = Color.Black
            )
        }

        Card (
            shape = RoundedCornerShape(30.dp),
            backgroundColor = Color("#F6F6F6".toColorInt()),
            modifier = Modifier
                .size(iconSize)

                .drawDropShadow(
                    Color.DarkGray,
                    0.5f,
                    topLeftBorderRadius = 100.dp,
                    topRightBorderRadius = 100.dp,
                    bottomLeftBorderRadius = 100.dp,
                    bottomRightBorderRadius = 100.dp,
                    shadowRadius = 2.dp,
                    offsetX = 2.dp,
                    offsetY = 2.dp,
                )
                .drawDropShadow(
                    Color("#FDFDFD".toColorInt()),
                    0.7f,
                    topLeftBorderRadius = 100.dp,
                    topRightBorderRadius = 100.dp,
                    bottomLeftBorderRadius = 100.dp,
                    bottomRightBorderRadius = 100.dp,
                    shadowRadius = 2.dp,
                    offsetX = -5.dp,
                    offsetY = 0.dp,
                )
                .clickable {
                    onRemoveClicked.invoke()
                }
        ){
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(17.dp),
                contentDescription = "",
                imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun LocalTrackFoldersMainPageContainer(list : State<List<File>?>,onSelectedListener:(File) ->Unit ){

    Box (modifier = Modifier
        .padding(bottom = 100.dp)
        .fillMaxWidth()
        .fillMaxHeight()
    ){
        GridView(
            2,
            list.value!!,
            scrollable = true
        ){ item ->
            FolderItem(item,onSelectedListener)
        }
    }
}
@Composable
fun FolderItem(file:File,onSelectedListener:(File) ->Unit ){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .drawDropShadow(
                    Color(0xFF090909),
                    0.5f,
                    topLeftBorderRadius = 5.dp,
                    topRightBorderRadius = 5.dp,
                    bottomLeftBorderRadius = 5.dp,
                    bottomRightBorderRadius = 5.dp,
                    shadowRadius = 3.dp,
                    offsetX = 5.dp,
                    offsetY = 5.dp,
                )
        ){
            Image(
                painter = painterResource(id = R.drawable.file_image),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
                    .clickable {
                        onSelectedListener.invoke(file)
                    }
            )
        }
        Text(
            text = file.name,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }


}