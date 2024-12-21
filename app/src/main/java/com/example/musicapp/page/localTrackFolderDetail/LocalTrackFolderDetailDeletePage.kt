package com.example.musicapp.page.localTrackFolderDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R
import com.example.musicapp.bean.local.EditingMP3Metadata
import com.example.musicapp.bean.local.MP3Metadata
import com.example.musicapp.components.CheckBox
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.components.drawInnerShadow
import com.example.musicapp.routing.Screen
import com.example.musicapp.tool.device.getScreenWidth
import com.example.musicapp.viewmodel.LocalTrackFolderDetailViewModel
import java.io.File

@Composable
fun  LocalTrackFolderDetailDeletePage(viewModel: LocalTrackFolderDetailViewModel, isRemoveMode: MutableState<Boolean>){
    val tracks = viewModel.getEditingFolderTracks().observeAsState()
    val isUpdate = remember{ mutableStateOf(false) }

    if( ! isUpdate.value){
        viewModel.updateFolderEditTracks()
        isUpdate.value = true
    }

    Column(
        modifier = Modifier
            .background(Color("#F4F4F4".toColorInt()))
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){
        LocalTrackFolderDetailMainPageEditTopBar(
            onBackClicked = {
                isRemoveMode.value = false
            },
            onConfirmClicked = {
                isRemoveMode.value = false
                viewModel.removeSelectedTrack()
            },
            onCancelAll = {
                viewModel.resetEditTracksState()
            },
            this
        )
        LocalTrackFolderDetailMainPageEditContainer(
            this,tracks.value!!,
            onCheckChanged = { editingMp3Data:EditingMP3Metadata,isSelected:Boolean->
                 viewModel.updateEditTracksState(editingMp3Data,isSelected)
            }
        )
    }

}

@Composable
fun LocalTrackFolderDetailMainPageEditTopBar(onBackClicked:()->Unit,onConfirmClicked:()->Unit, onCancelAll:()->Unit,columnScope: ColumnScope){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
    ){

        Icon(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically)
                .padding()
                .clickable {
                    onBackClicked.invoke()
                },
            contentDescription = "",
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_white)
        )
        Spacer(modifier = Modifier.width(60.dp))
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
                    onConfirmClicked.invoke()
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
                    onCancelAll.invoke()
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
fun LocalTrackFolderDetailMainPageEditContainer(columnScope: ColumnScope,tracks:List<EditingMP3Metadata>,onCheckChanged:(editingMp3Data:EditingMP3Metadata,isSelected:Boolean)->Unit){
    columnScope.apply{
        Surface(
            color = Color("#F4F4F4".toColorInt()),
            shape = RoundedCornerShape(topStart =50.dp,topEnd = 50.dp),
            modifier = Modifier
                .drawInnerShadow(
                    color = Color("#B6B6B6".toColorInt()),
                    1f,
                    left = 3f,
                    right = 3f,
                    top = 5f,
                    bottom = 0f,
                    blur = 1f,
                    topLeftBorderRadius = 50.dp,
                    topRightBorderRadius = 50.dp,
                    shadowRadius = 3.dp
                )
                .fillMaxHeight()
                .width(getScreenWidth() - 20.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)

        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                     .padding(bottom = 200.dp)
                    .verticalScroll(rememberScrollState())
            ){
                for(i in 0 until tracks.size){
                    LockTrackItemEditView(tracks[i],this,onCheckChanged)
                }
            }

        }
    }
}

@Composable
fun LockTrackItemEditView(file:EditingMP3Metadata,columnScope: ColumnScope,onCheckChanged:(editingMp3Data:EditingMP3Metadata,isSelected:Boolean)->Unit){
    columnScope.apply{
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        ){
            Row(
                modifier = Modifier.padding(5.dp)
            ){
                Column(
                    modifier = Modifier.weight(3f)
                ){
                    Text(
                        "${file.mP3Metadata.trackName}",
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start= 10.dp),

                        )
                    Text(
                        "${file.mP3Metadata.artistName}",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start= 10.dp),
                    )
                }
                CheckBox(
                    sideLength = 30.dp,
                    file.isSelected,
                ){
                       onCheckChanged.invoke(file, ! file.isSelected)
                  }
            }
            Divider(
                color = Color.Black,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}