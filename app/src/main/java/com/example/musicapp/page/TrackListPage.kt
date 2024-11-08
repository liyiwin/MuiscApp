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
import coil.compose.rememberAsyncImagePainter
import com.example.kkbox_music_app.components.dialog.FailureDialog
import com.example.kkbox_music_app.components.dialog.LoadingDialog
import com.example.kkbox_music_app.components.dialog.SuccessDialog
import com.example.musicapp.R
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.components.drawInnerShadow
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
    viewModel.resetData()
    viewModel.UpdatePlayList()
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
                .background(Color(0xFFF3F3F3))
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),

        ) {
            TrackListPageTitle(pageTitle,onBackPress = onBackPress)
            TrackListMainContainer(viewModel, OnSelected = {

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
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ){
        Surface(
            color = Color(0xFFF3F3F3),
            onClick = { onBackPress.invoke() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_black),
                contentDescription = "",
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .padding(top = 10.dp),
            )
        }
        Text(
            fontSize = 20.sp,
            text = pageTitle,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 70.dp, top = 8.dp, bottom = 8.dp)
                .align(Alignment.CenterVertically)

        )
        Image(
            painter = painterResource(id = R.drawable.ic_hit_tracks),
            contentDescription = "",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .padding(top = 10.dp)
                .align(Alignment.CenterVertically)
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
        color = Color(0xFFE1F3EE),
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .drawInnerShadow(
                color = Color.Gray,
                0.9f,
                left = 0F,
                top = 20F,
                right = 0F,
                bottom = 0F,
                topRightBorderRadius = 25.dp,
                topLeftBorderRadius = 25.dp,
                shadowRadius = 20.dp,
            )
    ){

        LazyColumn(
            state = listState,
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
        Box(Modifier.padding(15.dp)) {
            val padding = 20.dp
            val itemHeight = 120.dp

            Card(
                elevation = 10.dp,
                shape  = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .drawDropShadow(
                        Color.LightGray,
                        0.9f,
                        topLeftBorderRadius = 10.dp,
                        topRightBorderRadius = 10.dp,
                        bottomLeftBorderRadius = 10.dp,
                        bottomRightBorderRadius = 10.dp,
                        shadowRadius = 1.dp,
                        offsetX = 10.dp,
                        offsetY = 10.dp,
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
                            .fillMaxHeight()
                            .fillMaxWidth()
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
                        .clip(RoundedCornerShape(100.dp)),
                    painter = rememberAsyncImagePainter(track.album.images[1].url),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}