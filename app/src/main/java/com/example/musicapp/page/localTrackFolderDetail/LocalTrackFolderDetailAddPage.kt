package com.example.musicapp.page.localTrackFolderDetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.example.musicapp.R
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.tool.file.FileUtils
import com.example.musicapp.tool.requestExternalStoragePermission
import com.example.musicapp.view.statelessDataStorage.LocalTrackFolderDetailAddStatelessDataStorage
import com.example.musicapp.viewmodel.LocalTrackFolderDetailViewModel
import java.io.File


@Composable
fun LocalTrackFolderDetailAddPage(viewModel: LocalTrackFolderDetailViewModel, isAddMode: MutableState<Boolean>) {
        val statelessDataStorage = LocalTrackFolderDetailAddStatelessDataStorage(LocalContext.current)
       requestExternalStoragePermission(LocalContext.current as Activity)
       val scrollState = rememberScrollState()
      Column(
          modifier = Modifier
              .background(Color("#F4F4F4".toColorInt()))
              .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
              .padding(start = 20.dp, end = 20.dp)

      ) {
          Column (
              modifier = Modifier
//                  .padding(bottom = 50.dp)
                  .verticalScroll(state = scrollState)
                  .weight(8f)
          ){
              FileSelector(statelessDataStorage)
              TrackNameInput(statelessDataStorage)
              ArtistNameInput(statelessDataStorage)
              AlbumCoverSelector(statelessDataStorage)
          }

          ButtonContainer(
           modifier = Modifier.weight(1f).align(Alignment.CenterHorizontally)   ,
           onConfirm = {
               isAddMode.value = false
               if(statelessDataStorage.checkIsInputFinished()){
                   statelessDataStorage.saveTrack()
               }
          },
           onCancel = {
               isAddMode.value = false
          })
      }
}

@Composable
fun FileSelector(dataStorage: LocalTrackFolderDetailAddStatelessDataStorage){
    val context = LocalContext.current
    val name = remember{mutableStateOf("")}
    val launcher = rememberLauncherForActivityResult(  contract = ActivityResultContracts.OpenDocument() ) { uri ->
        uri?.let {
            val fileName = FileUtils.getFileName(it,context)
            dataStorage.trackUri = uri
            name.value = fileName
        }

    }
     Column (
         modifier = Modifier.padding(top =50.dp, bottom = 50.dp)
     ){

        Text(
            "上傳檔案",
            color = Color.Black,
            fontSize = 20.sp,
        )
         Surface (
             shape = RoundedCornerShape(20.dp),
             color = Color.White,
             modifier = Modifier
                 .fillMaxWidth()
                 .height(200.dp)
                 .padding(top = 50.dp)
                 .clickable {
                     launcher.launch(arrayOf("audio/*"))
                 }
         ){
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .align(Alignment.CenterHorizontally)
                   .padding(start = 50.dp)
           ){
               if(name.value.isEmpty()){
                   Icon(
                       modifier = Modifier
                           .size(30.dp)
                           .fillMaxWidth()
                           .align(Alignment.CenterVertically)
                           .weight(1f),
                       contentDescription = "",
                       imageVector = ImageVector.vectorResource(id = R.drawable.upload),
                       tint = Color.Black
                   )
                   Text(
                       "點擊上傳檔案",
                       color = Color.Black,
                       fontSize = 18.sp,
                       modifier = Modifier
                           .align(Alignment.CenterVertically)
                           .fillMaxWidth()
                           .weight(3f),
                   )
               }else{
                   Text(
                       name.value,
                       color = Color.Black,
                       fontSize = 18.sp,
                       modifier = Modifier
                           .align(Alignment.CenterVertically)
                           .fillMaxWidth(),
                   )
               }


           }
         }
     }
}

@Composable
fun TrackNameInput(dataStorage: LocalTrackFolderDetailAddStatelessDataStorage){
    var textValue by remember { mutableStateOf(TextFieldValue("")) }
    Column (
        modifier = Modifier.padding( bottom = 50.dp)
    ){
        Text(
            "輸入歌曲名稱",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding( bottom = 30.dp)
        )
        Surface (
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ){
            Surface (
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ){
                TextField(
                    value = textValue,
                    modifier = Modifier .fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = { newText ->
                        textValue = newText
                        dataStorage.trackName =  newText.text
                    }
                )
            }
        }
    }
}


@Composable
fun ArtistNameInput(dataStorage: LocalTrackFolderDetailAddStatelessDataStorage){
    var textValue by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier.padding(bottom = 50.dp)
    ) {
        Text(
            "輸入歌手名稱",
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding( bottom = 30.dp)
            )
        Surface (
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ){
            TextField(
                value = textValue,
                modifier = Modifier .fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = { newText ->
                    textValue = newText
                    dataStorage.artistName = newText.text
                }
            )
        }
    }
}

@Composable
fun AlbumCoverSelector(dataStorage: LocalTrackFolderDetailAddStatelessDataStorage){
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        dataStorage.coverImageUri = uri
    }
    Column(
        modifier = Modifier.padding(bottom = 50.dp)
    ){
        Text(
            "選擇專輯封面",
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding( bottom = 30.dp)
        )
        Surface (
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 50.dp)
                .clickable {
                    launcher.launch("image/*")
                }
        ){
            if( selectedImageUri != null){
                selectedImageUri!!.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.clip(shape = RoundedCornerShape(20.dp),)
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            }else{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 50.dp)
                ){
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        contentDescription = "",
                        imageVector = ImageVector.vectorResource(id = R.drawable.upload),
                        tint = Color.Black
                    )
                    Text(
                        "點擊上傳圖片",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth()
                            .weight(3f),
                    )


                }
            }

        }
    }
}


@Composable
fun ButtonContainer(modifier :Modifier,onConfirm:()->Unit,onCancel:()->Unit){
    Row(
        modifier = modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
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
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    onConfirm.invoke()
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
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    onCancel.invoke()
                }
        ){
            Text(
                text = "取消",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}

