package com.example.musicapp.page.localTrackFolderDetail

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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R
import com.example.musicapp.bean.local.MP3Metadata
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.components.drawInnerShadow
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.tool.TimeConverter
import com.example.musicapp.tool.device.getScreenWidth
import com.example.musicapp.viewmodel.LocalTrackFolderDetailViewModel
import java.io.File

@Composable
fun LocalTrackFolderDetailMainPage(viewModel: LocalTrackFolderDetailViewModel, navigationController:(screen: Screen)->Unit, isAddMode: MutableState<Boolean>, isRemoveMode:MutableState<Boolean>) {
    val tracks = viewModel.getFolderTracks().observeAsState()
    val isUpdate = remember{ mutableStateOf(false) }
    if( ! isUpdate.value){
        viewModel.updateFolderTracks()
        isUpdate.value = true
    }
    Column(
        modifier = Modifier
            .background(Color("#F4F4F4".toColorInt()))
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){
        LocalTrackFolderDetailMainPageTopBar(
            onBackClicked = {
               navigationController.invoke(Screen.LocalTrackFoldersScreen())
            },
            onAddClicked = {
                isAddMode.value = true
            },
            onRemoveClicked = {
                isRemoveMode.value = true
            },
            this
        )
        LocalTrackFolderDetailMainPageContainer(
            this,tracks.value!!
        ){ selectedIndex ->
            RouterDataStorage.localTrackIndex = selectedIndex
            navigationController.invoke(Screen.MusicPlayerScreen())
        }
    }



}

@Composable
fun LocalTrackFolderDetailMainPageTopBar(onBackClicked:()->Unit,onAddClicked:()->Unit, onRemoveClicked:()->Unit,columnScope: ColumnScope){
    columnScope.apply{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement =Arrangement.spacedBy(15.dp)
        ){
            val iconSize = 60.dp

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .clickable{
                        onBackClicked.invoke()
                    },
                contentDescription = "",
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_white)
            )
           Spacer(modifier = Modifier.weight(2f))

            Card (
                shape = RoundedCornerShape(30.dp),
                backgroundColor = Color("#F6F6F6".toColorInt()),
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
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
                    .weight(1f)
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

}
@Composable
fun LocalTrackFolderDetailMainPageContainer(columnScope: ColumnScope,tracks:List<MP3Metadata>,onItemSelected:(Int) ->Unit){
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
                 .width(getScreenWidth()-20.dp)
                 .align(Alignment.CenterHorizontally)
                 .padding(top = 10.dp ,start = 10.dp,end = 10.dp )

         ){
             Column(
                 modifier = Modifier
                     .fillMaxSize()
//                     .padding(bottom = 200.dp)
                     .verticalScroll(rememberScrollState())
             ){
                 for(i in 0 until tracks.size){
                     LockTrackItemView(this,tracks[i],i,onItemSelected)
                 }
              }

         }
     }
}

@Composable
fun LockTrackItemView(columnScope: ColumnScope,track:MP3Metadata,index:Int,onItemSelected:(Int) ->Unit){
    columnScope.apply{
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp)
                .clickable {
                    onItemSelected.invoke(index)
                }
        ){
            Row(
                modifier = Modifier.padding(5.dp)
            ){
                Column(
                    modifier = Modifier.weight(3f)
                ){
                    Text(
                        "${track.trackName}",
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start= 10.dp),

                    )
                    Text(
                        "${track.artistName}",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start= 10.dp),
                    )
                }
                Text(
                    TimeConverter.convertTimeLongToText(track.duration?:0),
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                )
                Card(
                    contentColor = Color("#F4F4F4".toColorInt()),
                    modifier = Modifier.border(1.dp,Color.Black,RoundedCornerShape(100.dp))
                        .align(Alignment.CenterVertically)
                        .size(40.dp)
                        .padding(start = 13.dp,end = 10.dp,top =10.dp, bottom =10.dp ),
                    elevation = 0.dp

                ){
                    Icon(
                      modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .background(color = Color("#F4F4F4".toColorInt())),
                        contentDescription = "",
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_play_two),
                        tint = Color.Black,
                    )
                }

            }
            Divider(
                color = Color.Black,
                modifier = Modifier.fillMaxSize()

            )
        }
    }

}