package com.example.musicapp.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R
import com.example.musicapp.bean.local.MP3Metadata
import com.example.musicapp.bean.local.PlayControllerType
import com.example.musicapp.components.ProgressBar
import com.example.musicapp.components.drawInnerShadow
import com.example.musicapp.routing.Screen
import com.example.musicapp.tool.MusicPlayer.MusicPlayer
import com.example.musicapp.tool.TimeConverter
import com.example.musicapp.viewmodel.MusicPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun  MusicPlayerPage(viewModel: MusicPlayerViewModel,navigationController:(Screen) -> Unit){
    val musicPlayer = remember { mutableStateOf(MusicPlayer()) }
    val currentTrack = viewModel.getCurrentTrack.observeAsState()
    val isPause =viewModel.isPause.observeAsState()
    val isCycle = viewModel.isCycle.observeAsState()
    val isRandom = viewModel.isRandom.observeAsState()

    LaunchedEffect(Unit){
        viewModel.loadMusicData()
    }
   currentTrack.value?.apply{
           Log.d("MusicPlayerPage","$currentTrack")
          if(musicPlayer.value.checkIsNotSetting()){
              musicPlayer.value.setMusic(originFile){}
              musicPlayer.value.init()
              musicPlayer.value.setMusicCompletedCallback {
                  CoroutineScope(Dispatchers.Main).launch {
                      viewModel.playNextTrack()
                      Log.d("MusicCompleted","${currentTrack}")
                      currentTrack.value?.apply {
                          musicPlayer.value.setMusic(originFile){
                              musicPlayer.value.startMusic()
                              Log.d("MusicPlayer startMusic","startMusic（3）")
                          }
                      }
                  }
              }
          }
     }


    MusicPlayerPageContent(musicPlayer.value,track = currentTrack.value,isPause.value!!,isCycle.value!!,isRandom.value!!
         ,onBackPress ={
            navigationController.invoke(Screen.LocalTrackFolderDetailPage())
        }
         ,playController = { type: PlayControllerType ->
             when(type){
                 PlayControllerType.Cycle -> {
                    viewModel.triggerCycle()
                 }
                 PlayControllerType.Previous -> {
                     viewModel.jumpToLastTrack()
                     currentTrack.value?.apply {
                         musicPlayer.value.setMusic(originFile){
                             if( ! viewModel.isPause.value!!){
                                 Log.d("MusicPlayer startMusic","startMusic（5）")
                                 musicPlayer.value.startMusic()
                             }
                         }
                         musicPlayer.value.updateProgress(0f)
                     }
                 }
                 PlayControllerType.ChangePlayState -> {
                     viewModel.changePlayState()
                     Log.d("playerState+++", "${ viewModel.isPause.value!! }")
                     if(viewModel.isPause.value!!){
                         musicPlayer.value.pauseMusic()
                     }else{
                         Log.d("MusicPlayer startMusic","startMusic（2）")
                         musicPlayer.value.startMusic()
                     }
                 }
                 PlayControllerType.Next -> {
                    viewModel.jumpToNextTrack()
                     currentTrack.value?.apply {
                         musicPlayer.value.setMusic(originFile){
//                             Log.d("PlayControllerType.Next","${viewModel.isPause.value!!}")
                             if( ! viewModel.isPause.value!!){
                                 Log.d("MusicPlayer startMusic","startMusic（4）")
                                 musicPlayer.value.startMusic()
                             }
                         }
                         musicPlayer.value.updateProgress(0f)
                     }
                 }
                 PlayControllerType.Random -> {
                      viewModel.triggerRandom()
                 }
             }
         }
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.cleanData()
           musicPlayer.value.releaseData()
        }
    }
}

@Composable
fun MusicPlayerPageContent (
                             musicPlayer :MusicPlayer,
                             track: MP3Metadata?
                            ,isPause:Boolean
                            ,isCycle:Boolean
                            ,isRandom:Boolean
                            ,onBackPress:()->Unit
                            ,playController:(type:PlayControllerType)->Unit
){
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){

        TrackCovertImage(
            track,
            modifier = Modifier
                .height(screenWidth.dp)
                .width(screenWidth.dp)
        )
        PlayControllerView(
            musicPlayer,
            track,
            isPause,
            isCycle,
            isRandom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = (screenHeightDp - screenWidth - 10).dp)
            ,playController
        )
        Image(
            painter = painterResource(id = R.drawable.ic_back_white),
            contentDescription = "",
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .align(Alignment.TopStart)
                .clickable {
                    onBackPress.invoke()

                },
        )

    }
}

@Composable
fun TrackCovertImage(track: MP3Metadata?, modifier:Modifier){
    Surface(
        modifier = modifier,
        color = Color("#F5FFFA".toColorInt())
    ){
        track?.coverImage?.let{ bitmap ->
            Image(
                painter = BitmapPainter(bitmap.asImageBitmap()),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
   }
}

 @Composable
fun PlayControllerView(
                        musicPlayer :MusicPlayer,
                        track: MP3Metadata?
                        ,isPause:Boolean
                        ,isCycle:Boolean
                        ,isRandom:Boolean
                       ,modifier:Modifier
                       ,playController:(type: PlayControllerType)->Unit){
                  val currentProgress = remember { mutableStateOf(0f) }
                  val currentPositionText = remember{ mutableStateOf("00:00") }
                  musicPlayer.setProgressChangeListener {progress:Float,currentPosition:String->
                      currentProgress.value = progress
                      currentPositionText.value = currentPosition
                  }


                 Surface(
                color = Color.White,
                modifier = modifier
                    .drawInnerShadow(
                        color = Color("#B6B6B6".toColorInt()),
                        1f,
                        left = 3f,
                        right = 3f,
                        top = 10f,
                        bottom = 0f,
                        blur = 1f,
                        shadowRadius = 3.dp
                    )
                    .padding(top = 10.dp, end = 3.dp, start = 3.dp)
            ){
                  Column(
                      modifier = Modifier.padding(10.dp)
                  ){
                    Text(
                        track?.trackName?:"",
                        fontSize = 25.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                        )
                    Text(track?.artistName?:"",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f)
                      )

                      Row(
                          modifier = Modifier.weight(1f)
                      ){
                        track?.duration?.let{
                          Text(
                              currentPositionText.value,
                              fontSize = 15.sp,
                              color = Color.Gray,
                              textAlign = TextAlign.Start,
                              modifier = Modifier
                                  .weight(1f)
                          )
                          Text(
                              TimeConverter.convertTimeLongToText(it),
                              fontSize = 15.sp,
                              color = Color.Gray,
                              textAlign = TextAlign.End,
                              modifier = Modifier
                                  .weight(1f)
                          )
                      }

                      }
                      ProgressBar(
                          progress = currentProgress.value,
                          modifier = Modifier,
                          onDragStart = {
                              if( ! isPause){
                                  Log.d("ProgressBar","pauseMusic")
                                  playController.invoke(PlayControllerType.ChangePlayState)
                                  musicPlayer.pauseMusic()
                              }
                          },
                          onDragging = { progress: Float ->
                              currentProgress.value = progress
                              musicPlayer.updateProgress(progress)
                          },
                          onDragEnd = {progress: Float ->
                              Log.d("ProgressBar","$progress")
                              musicPlayer.seekToPosition(progress)
                               if( ! isPause){
                                   Log.d("MusicPlayer startMusic","startMusic（1）")
                                   musicPlayer.startMusic()
                               }
                          }
                      )
                      Row(
                          modifier = Modifier
                              .weight(3f)
                              .align(Alignment.CenterHorizontally)
                      ){
                          Icon(
                              painter = painterResource(id = R.drawable.ic_loop),
                              tint = if(isCycle)Color.Gray else Color.LightGray,
                              contentDescription = "",
                              modifier = Modifier
                                  .height(40.dp)
                                  .width(40.dp)
                                  .align(Alignment.CenterVertically)
                                  .weight(1f)
                                  .clickable {
                                      playController.invoke(PlayControllerType.Cycle)
                                  },
                          )
                          Icon(
                              painter = painterResource(id = R.drawable.ic_previous_music),
                              tint = Color.Gray,
                              contentDescription = "",
                              modifier = Modifier
                                  .height(40.dp)
                                  .width(40.dp)
                                  .align(Alignment.CenterVertically)
                                  .weight(1f)
                                  .clickable {
                                      if (!musicPlayer.isPreparing) playController.invoke(
                                          PlayControllerType.Previous
                                      )
                                  },
                          )
                          Box(
                              modifier = Modifier
                                  .align(Alignment.CenterVertically)
                                  .size(60.dp) // 設定大小
                                  .clip(CircleShape)
                                  .padding(1.dp)
                                  .weight(1f)
                                  .clickable {
                                      playController.invoke(PlayControllerType.ChangePlayState)
                                  }
                          ){
                              Icon(
                                  painter = painterResource(id = if(isPause) R.drawable.ic_play_gray  else R.drawable.ic_pause_gray),
                                  tint = Color.Gray,
                                  contentDescription = "",
                                  modifier = Modifier
                                      .height(40.dp)
                                      .width(40.dp)
                                      .padding(start = 7.dp)
                                      .align(Alignment.Center) ,
                              )

                          }

                          Icon(
                              painter = painterResource(id = R.drawable.ic_next_music),
                              tint = Color.Gray,
                              contentDescription = "",
                              modifier = Modifier
                                  .height(40.dp)
                                  .width(40.dp)
                                  .align(Alignment.CenterVertically)
                                  .weight(1f)
                                  .clickable {
                                      if (!musicPlayer.isPreparing) playController.invoke(
                                          PlayControllerType.Next
                                      )
                                  },
                          )
                          Icon(
                              painter = painterResource(id = R.drawable.ic_random_play),
                              tint = if(isRandom) Color.Gray else Color.LightGray,
                              contentDescription = "",
                              modifier = Modifier
                                  .height(40.dp)
                                  .width(40.dp)
                                  .align(Alignment.CenterVertically)
                                  .weight(1f)
                                  .clickable {
                                      playController.invoke(PlayControllerType.Random)
                                  },
                          )
                      }
                  }
            }
}