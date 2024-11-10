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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
import com.example.musicapp.components.GridView
import com.example.musicapp.components.GridViewPager
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.components.drawInnerShadow
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.HomeViewModel
import com.example.musicapp.viewmodel.RecommendViewModel
import com.example.musicapp.viewmodel.state.RequestState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  RecommendPage(viewModel: RecommendViewModel, navigationController:(screen: Screen)->Unit){

    viewModel.fetchDailyRecommendedTracks()
    viewModel.fetchPersonalRecommendedTracks()

    RequestStateDialog(viewModel)

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(
        state = swipeRefreshState ,
        onRefresh = {
            viewModel.fetchDailyRecommendedTracks()
            viewModel.fetchPersonalRecommendedTracks()
        }){
        Column(
            modifier = Modifier
                .background(Color("#FFF9F9".toColorInt()))
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),

            ){
            RecommendedPageTitle()
            RecommendedPageContainer(viewModel, OnSelected = {
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
fun RequestStateDialog(viewModel: RecommendViewModel){
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
fun RecommendedPageTitle(){
    Text(
        fontSize = 20.sp,
        text = "推薦清單",
        textAlign = TextAlign.Center,
        color = Color.Black,
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecommendedPageContainer(viewModel: RecommendViewModel,OnSelected:(Track)->Unit){
    Surface(
        shape = RoundedCornerShape( topStart = 20.dp,topEnd= 20.dp),
        color = Color(0xFFFFFFFF),
        modifier = Modifier
            .padding(top = 30.dp, start = 5.dp,end = 5.dp)
//            .border(1.dp,Color.Gray, RoundedCornerShape(topStart = 20.dp,topEnd= 20.dp))
            .fillMaxWidth()
            .fillMaxHeight()
            .drawDropShadow(
                Color(0xFF090909),
                0.5f,
                topLeftBorderRadius = 20.dp,
                topRightBorderRadius = 20.dp,
                bottomLeftBorderRadius = 20.dp,
                bottomRightBorderRadius = 20.dp,
                shadowRadius = 3.dp,
                offsetX = 6.dp,
                offsetY = -5.dp,
            ),
    ){
        LazyColumn{

            item{
                DailyRecommendTracksTitle()
            }

            item{
                DailyRecommendTracksContainer(viewModel,OnSelected)
            }

            item{
                personalRecommendTracksTitle()
            }

            item{
                personalRecommendTracksContainer(viewModel,OnSelected)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyRecommendTracksTitle(){
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .padding(10.dp),
        horizontalArrangement =  Arrangement.Start
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight(),
        ){
            Text(
                modifier = Modifier.align(Alignment.Center),
                fontSize = 20.sp,
                text = "每日推薦歌曲",
                color = Color.Black
            )
        }
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_daily),
            contentDescription = ""
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyRecommendTracksContainer(viewModel: RecommendViewModel,OnSelected:(Track)->Unit){
    val pageState  = rememberLazyListState()
    val dailyRecommendedTracks = viewModel.getLatestDailyRecommendedTracks().observeAsState()
    Box(
        modifier = Modifier.height(250.dp)
    ){
        LazyRow (
            state = pageState,
            modifier = Modifier
                .fillMaxHeight()
        ){
            GridViewPager(
                dailyRecommendedTracks.value!!,
                3,
                2
            ){  item ->
                RecommendTracksItem(item,OnSelected)
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun personalRecommendTracksTitle(){
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .padding(10.dp) ,
        horizontalArrangement =  Arrangement.Start
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight(),
        ){
            Text(
                modifier = Modifier.align(Alignment.Center),
                fontSize = 20.sp,
                text = "個人推薦",
                color = Color.Black
            )
        }
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_personal_two),
            contentDescription = ""
        )


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun personalRecommendTracksContainer(viewModel: RecommendViewModel,OnSelected:(Track)->Unit){
    val personalRecommendTracks =  viewModel.getLatestPersonalRecommendedTracks().observeAsState();
    Box (modifier = Modifier.padding(bottom = 100.dp)){
        GridView(
            2,
            personalRecommendTracks.value!!,
            scrollable = false
        ){ item ->
            RecommendTracksItem(item,OnSelected)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecommendTracksItem(item:Track,OnSelected:(Track)->Unit){
    Surface(
        // backgroundColor = Color.LightGray,
        shape  = RoundedCornerShape(10.dp),
        color = Color.White,
        modifier = Modifier
            .padding(10.dp)
            .drawDropShadow(
                Color.DarkGray,
                0.9f,
                topLeftBorderRadius = 10.dp,
                topRightBorderRadius = 10.dp,
                bottomLeftBorderRadius = 10.dp,
                bottomRightBorderRadius = 10.dp,
                shadowRadius = 6.dp,
                offsetX = 5.dp,
                offsetY = 5.dp,
            )
    ){
        Image(
            painter = rememberAsyncImagePainter(model = item.album.images.get(1).url),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        OnSelected(item)
                    }
                )
        )
    }

}