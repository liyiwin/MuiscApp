package com.example.musicapp.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.example.kkbox_music_app.components.dialog.FailureDialog
import com.example.kkbox_music_app.components.dialog.LoadingDialog
import com.example.kkbox_music_app.components.dialog.SuccessDialog
import com.example.musicapp.R
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.components.isScrolledToEnd
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.TrackListViewModel
import com.example.musicapp.viewmodel.state.RequestState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackListPage(pageTitle:String , viewModel: TrackListViewModel, onBackPress:()->Unit, navigationController:(screen: Screen)->Unit){
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    LaunchedEffect(Unit) {
        viewModel.resetData()
        viewModel.UpdatePlayList()
    }
    RequestStateDialog(viewModel)

    SwipeRefresh(
        state = swipeRefreshState ,
        onRefresh ={
            viewModel.resetData()
            viewModel.UpdatePlayList()
        }
    ) {
        Column(
            modifier = Modifier
                .background(Color("#E4FFFF".toColorInt()))
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),

        ) {
            TrackListPageTitle(pageTitle,onBackPress = onBackPress)
            TrackListMainContainer(viewModel, OnSelected = {
                RouterDataStorage.putTrack(it)
                navigationController.invoke(Screen.TrackDetailScreen())
            })
        }
    }

}


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestStateDialog(viewModel: TrackListViewModel){
    val requestState = viewModel.getRequestDisplayState().observeAsState()
    requestState.value?.let {
        when(it){

            is RequestState.Loading ->{
                LoadingDialog(modifier = Modifier)
            }

            is RequestState.Success ->{
                SuccessDialog(title = "", message = it.message, modifier = Modifier) {
                    viewModel.clearRequestDisplayState()
                }
            }

            is RequestState.Failure ->{
                FailureDialog(title = "加載失敗", message =it.message, modifier = Modifier){
                    viewModel.clearRequestDisplayState()
                }
            }

            is RequestState.None->{

            }

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun TrackListPageTitle(pageTitle:String,onBackPress:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_back_black),
            contentDescription = "",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .padding(top = 10.dp)
                .weight(1f)
                .clickable {
                    onBackPress.invoke()
                }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_hit_tracks),
            contentDescription = "",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .align(Alignment.CenterVertically)
                .padding(top = 8.dp, start = 5.dp,end = 5.dp)
                .weight(2f)
        )
        Text(
            fontSize = 20.sp,
            text = pageTitle,
            textAlign = TextAlign.Start,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding( top = 8.dp, bottom = 8.dp)
                .weight(6f)

       )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackListMainContainer(viewModel: TrackListViewModel,OnSelected:(Track)->Unit){
    val tracks = viewModel.getTotalTracks().observeAsState()
    val listState = rememberLazyListState()
    Surface(
        shape  = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp),
        color = Color("#FFFFFF".toColorInt()),
        modifier = Modifier
            .padding(top = 30.dp, start = 10.dp,end =10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .border(0.1.dp,Color.Black,RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
   ){

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 50.dp,bottom = 100.dp),
        ) {
            for(i in tracks.value!!.indices)
                item{
                    TrackItem(tracks.value!![i],OnSelected)
                }
        }
    }

    // observer when reached end of list
    val endOfListReached by remember { derivedStateOf { listState.isScrolledToEnd() } }
    LaunchedEffect(endOfListReached) {
        if(endOfListReached) viewModel.LoadPlayList()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackItem(track: Track, OnSelected:(Track)->Unit){
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp, bottom = 5.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    OnSelected(track)
                }
            )
        ,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.padding(15.dp).background(Color("#F6FFF6".toColorInt()))) {
            val padding = 20.dp
            val itemHeight = 120.dp

            Card(
                elevation = 10.dp,
                shape  = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .drawDropShadow(
                        Color.LightGray,
                        0.9f,
                        topLeftBorderRadius = 20.dp,
                        topRightBorderRadius = 20.dp,
                        bottomLeftBorderRadius = 20.dp,
                        bottomRightBorderRadius = 20.dp,
                        shadowRadius = 0.5.dp,
                        offsetX = 5.dp,
                        offsetY = 5.dp,
                    )
                    .height(IntrinsicSize.Min)
                ,
            ) {
                Row( ){
                    Column(
                        modifier = Modifier
                            .width(250.dp)
                            .padding(
                                start = itemHeight + 30.dp ,
                                top = 30.dp,
                                bottom = 30.dp
                            )

                    ) {
                        Text(text = track.name,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = track.album.artist.name,
                            color = Color.LightGray,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_play_music),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(
                                start = 25.dp,
                                top = (itemHeight - 50.dp) / 2,
                                bottom = (itemHeight - 50.dp) / 2,
                                end = 25.dp
                            )
                            .height(30.dp)
                            .width(30.dp)
                        ,
                        // alignment = Alignment.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = padding, y = -itemHeight / 4),
            ){
                Image(
                    modifier = Modifier
                        .size(itemHeight)
                        .clip(RoundedCornerShape(100.dp))

                    ,
                    painter = rememberAsyncImagePainter(track.album.images[1].url),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}