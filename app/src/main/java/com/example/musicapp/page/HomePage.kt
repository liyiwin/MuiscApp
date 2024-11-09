package com.example.musicapp.page


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.kkbox_music_app.components.dialog.FailureDialog
import com.example.kkbox_music_app.components.dialog.LoadingDialog
import com.example.kkbox_music_app.components.dialog.SuccessDialog
import com.example.musicapp.bean.local.TrackListTransferData
import com.example.musicapp.components.GridViewPager
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.components.drawInnerShadow
import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.HomeViewModel
import com.example.musicapp.viewmodel.state.RequestState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(viewModel: HomeViewModel, navigationController:(screen: Screen,pageTitle:String)->Unit){
    viewModel.UpdateTotalChartList()
    viewModel.UpdateTotalFeaturedPlayListCategories()
    viewModel.UpdateTotalHitMusicList()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    RequestStateDialog(viewModel)

    SwipeRefresh(
        state = swipeRefreshState ,
        onRefresh = {
            viewModel.UpdateTotalChartList()
            viewModel.UpdateTotalFeaturedPlayListCategories()
            viewModel.UpdateTotalHitMusicList()
        }) {
        Column(
            modifier = Modifier
                .background(Color(0xFFDCFDFF))
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
        ){
            ChartList(viewModel,
                onChartSelected = { it ->
                    RouterDataStorage.putTrackListTransferData(TrackListTransferData.ChartPlayListTracksTransferData(it.id))
                    navigationController.invoke(Screen.TrackListScreen(),it.title)
                })
            MainContainer(viewModel,
                onFeaturedListSelected = {
                    RouterDataStorage. putTrackListTransferData(TrackListTransferData.FeaturedPlayListTracksTransferData(it.id))
                    navigationController.invoke(Screen.TrackListScreen(),it.title)
                },
                onHitPlayListSelected = {
                    RouterDataStorage. putTrackListTransferData(TrackListTransferData.FeaturedPlayListTracksTransferData(it.id))
                    navigationController.invoke(Screen.TrackListScreen(),it.title)
                }
            )
        }
    }

}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestStateDialog(viewModel: HomeViewModel){
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChartList(viewModel: HomeViewModel,onChartSelected:(PlayList) -> Unit){
    val scrollState = rememberScrollState()
    val chartlist = viewModel.getTotalChartList().observeAsState()
    Row(
        modifier = Modifier.horizontalScroll(
            state = scrollState,
        )
    ){
        for( i in chartlist.value!!.indices){
            RoundCornerItemView(chartlist.value!![i].images[1].url){
                onChartSelected.invoke(chartlist.value!![i])
            }
        }
    }
}

@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  RoundCornerItemView(url:String, onClickListener:()->Unit){

    Surface(
        // backgroundColor = Color.LightGray,
        shape  = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier
            .padding(10.dp)
            .drawDropShadow(
                Color.DarkGray,
                0.9f,
                topLeftBorderRadius = 20.dp,
                topRightBorderRadius = 20.dp,
                bottomLeftBorderRadius = 20.dp,
                bottomRightBorderRadius = 20.dp,
                shadowRadius = 6.dp,
                offsetX = 5.dp,
                offsetY = 5.dp,
            ),
        onClick = onClickListener
    ){
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(20.dp)),

            )
    }
}


@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  RoundCornerItemViewWithText(url:String, title:String,content:String,onClickListener:()->Unit){

    Surface(
        // backgroundColor = Color.LightGray,
        shape  = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier
            .padding(10.dp)
            .drawDropShadow(
                Color.DarkGray,
                0.9f,
                topLeftBorderRadius = 20.dp,
                topRightBorderRadius = 20.dp,
                bottomLeftBorderRadius = 20.dp,
                bottomRightBorderRadius = 20.dp,
                shadowRadius = 6.dp,
                offsetX = 5.dp,
                offsetY = 5.dp,
            ),
        onClick = onClickListener
    ){
        Column{
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp,20.dp,20.dp,0.dp)),

                )
            Text(
                fontSize = 23.sp,
                text = title,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start).padding(5.dp).fillMaxWidth()
            )
            Text(
                fontSize = 18.sp,
                text = content,
                textAlign = TextAlign.Start,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start).padding(5.dp).fillMaxWidth()
            )
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContainer(
    viewModel: HomeViewModel,
    onFeaturedListSelected:(PlayList) -> Unit,
    onHitPlayListSelected:(PlayList) -> Unit,
){
    Surface(
        shape  = RoundedCornerShape(topEnd = 50.dp),
        color = Color.White,
        modifier = Modifier
            .padding(start = 25.dp, end = 25.dp, top = 25.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .drawInnerShadow(
                color = Color.DarkGray,
                0.9f,
                left = 15F,
                top = 20F,
                right = 0F,
                bottom = 0F,
                blur = 1F,
                topRightBorderRadius = 50.dp,
                shadowRadius = 10.dp,
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(5.dp)
                .verticalScroll(rememberScrollState())
        ){

            FeaturedPlayListCategoriesContainer(viewModel, onFeaturedListSelected)
            HitTracksContainer(viewModel,onHitPlayListSelected)

        }
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun FeaturedPlayListCategoriesContainer(viewModel: HomeViewModel,onFeaturedListSelected:(PlayList) -> Unit){

    val pageState  = rememberLazyListState()
    val featuredPlayList = viewModel.getTotalFeaturedPlayLists().observeAsState()
    Text(
        color = Color.Gray,
        fontSize = 20.sp,
        text = "精選播放列表",
        modifier = Modifier.padding(top =8.dp,bottom = 8.dp)
    )

    Divider( color = Color.Gray, thickness = 1.dp)

    Box(
        modifier = Modifier.height(200.dp)
    ){
        LazyRow (
            state = pageState
        ){

            GridViewPager(
                featuredPlayList.value !!,
                3,
                2
            ){
                RoundCornerItemView(it.images[1].url){
                    onFeaturedListSelected.invoke(it)
                }
            }

        }
    }
    val itemCount = featuredPlayList .value!!.size
    val fillPageCount = itemCount/6
    val realPageCount:Int

    if(itemCount % 6 != 0)realPageCount = fillPageCount+1
    else realPageCount = fillPageCount

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        for( i in 0 until realPageCount ){
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .size(size = 10.dp)
                    .clip(shape = RoundedCornerShape(size = 100.dp))
                    .background(color = if (i == pageState.firstVisibleItemIndex) Color.DarkGray else Color.LightGray),
            )
        }
    }
}



@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HitTracksContainer(viewModel: HomeViewModel,onHitPlayListSelected:(PlayList) -> Unit){
    val hitTracksList = viewModel.getTotalHitMusicList().observeAsState()
    Text(
        color = Color.Gray,
        fontSize = 20.sp,
        text = "熱門曲目",
        modifier = Modifier.padding(top =8.dp,bottom = 8.dp)
    )

    Divider( color = Color.Gray, thickness = 1.dp)

    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        for( i in hitTracksList.value!!.indices){
            val imageUrl = hitTracksList.value!![i].images[1].url
            val title  = hitTracksList.value!![i].title
            val description = hitTracksList.value!![i].description
            RoundCornerItemViewWithText(imageUrl,title,description){
                onHitPlayListSelected.invoke(hitTracksList.value!![i])
            }
        }
    }
}