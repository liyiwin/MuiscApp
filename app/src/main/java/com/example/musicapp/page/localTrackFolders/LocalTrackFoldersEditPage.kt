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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R
import com.example.musicapp.bean.local.EditingFile
import com.example.musicapp.components.CheckBox
import com.example.musicapp.components.GridView
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.viewmodel.LocalTrackFoldersVIewModel

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocalTrackFoldersEditPage(vIewModel: LocalTrackFoldersVIewModel, isRemoveMode: MutableState<Boolean>) {
    if(vIewModel.getTotalEditingPlayLists().value!!.isEmpty()){
         vIewModel.updateTotalEditingPlayLists()
     }
    val list =vIewModel.getTotalEditingPlayLists().observeAsState()
    Column(
    modifier = Modifier
    .background(Color("#F4F4F4".toColorInt()))
    .fillMaxSize()
    .padding(10.dp)
    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){
        LocalTrackFoldersEditPageTopBar(
            onConfirmSelected = {
                vIewModel.removeSelectedPlayList()
                isRemoveMode.value = false

            }, onCancelAllSelected = {
                 vIewModel.resetEditingPlayList()
            }
        )
        LocalTrackFoldersEditPageContainer(list.value!!){ item, isSelected ->
              vIewModel.updateEditingPlayList(item,isSelected)
        }

    }
}


@Composable
fun LocalTrackFoldersEditPageTopBar(onConfirmSelected:()->Unit, onCancelAllSelected:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
    ){
        Card (
            shape = RoundedCornerShape(30.dp),
            backgroundColor = Color("#F6F6F6".toColorInt()),
            modifier = Modifier
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
                    onConfirmSelected.invoke()
                }
        ){
            Text(
                text = "確認",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(15.dp)
            )
        }

        Card (
            shape = RoundedCornerShape(30.dp),
            backgroundColor = Color("#F6F6F6".toColorInt()),
            modifier = Modifier
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
                    onCancelAllSelected.invoke()
                }
        ){
            Text(
                text = "全部取消",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}

@Composable
fun LocalTrackFoldersEditPageContainer(list : List<EditingFile>, onCheckChange:(EditingFile, Boolean)->Unit){
    Box (modifier = Modifier
        .padding(bottom = 100.dp)
        .fillMaxWidth()
        .fillMaxHeight()
    ){
        GridView(
            2,
            list,
            scrollable = true
        ){ item ->
            Row{
                CheckBox(25.dp,item.isSelected){ isChecked ->
                    onCheckChange(item,isChecked)
                }
                FolderEditItem(item.file.name)
            }

        }
    }
}
@Composable
fun FolderEditItem(folderName:String){
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

                    }
            )
        }
        Text(
            text = folderName,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }


}