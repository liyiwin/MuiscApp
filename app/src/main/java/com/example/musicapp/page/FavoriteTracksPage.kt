package com.example.musicapp.page

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.example.musicapp.R
import com.example.musicapp.bean.local.FavoriteTrack
import com.example.musicapp.components.drawDropShadow
import com.example.musicapp.viewmodel.FavoriteTracksViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun  FavoriteTracksPage(viewModel: FavoriteTracksViewModel, onBackPress:()->Unit,externalPageNavigationController:(uri: Uri)->Unit) {

    LaunchedEffect(Unit){
        viewModel.updateTotalFavoriteTracks()
    }

    Column(
        modifier = Modifier
            .background(Color("#EBE8E8".toColorInt()))
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)),
    ){
        favoriteTracksPageTitle(onBackPress)
        favoriteTracksPageContent(viewModel,
          removeListener = {track ->
              viewModel.removeFavoriteTrack(track)
          },
          externalPageNavigationController
        )
    }



}

@Composable
fun favoriteTracksPageTitle(onBackPress: () -> Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 10.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_back_white),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
                    onBackPress.invoke()
                }
        )
        Row (
            modifier = Modifier
                .height(70.dp)
                .padding(start = 50.dp)
                .weight(5f)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ){
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_storage),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "收藏歌曲",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }

    }

}

@Composable
fun favoriteTracksPageContent(viewModel: FavoriteTracksViewModel,removeListener:(FavoriteTrack)->Unit,externalPageNavigationController:(uri: Uri)->Unit) {
    val favoriteTracks = viewModel.getTotalFavoriteTracks().observeAsState()
    val listState = rememberLazyListState()
    LazyColumn( state = listState ,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for(i in 0 until favoriteTracks.value!!.size){
            val track = favoriteTracks.value!![i]
            item{
                ItemView(track,removeListener, externalPageNavigationController)
            }
        }
    }
}

@Composable
fun ItemView(track: FavoriteTrack,removeListener:(FavoriteTrack)->Unit,externalPageNavigationController:(uri: Uri)->Unit ){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .horizontalScroll(state = scrollState)
            .padding( end = 20.dp)
            .fillMaxHeight(),
    ){
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        Card (
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color("#F9F9F9".toColorInt()),
            elevation = 2.dp,
            modifier = Modifier
                .width(screenWidthDp)
                .padding(start = 40.dp, end = 40.dp)
                .drawDropShadow(
                    Color.DarkGray,
                    0.7f,
                    topLeftBorderRadius = 10.dp,
                    topRightBorderRadius = 10.dp,
                    bottomLeftBorderRadius = 10.dp,
                    bottomRightBorderRadius = 10.dp,
                    shadowRadius = 2.dp,
                    offsetX = 2.dp,
                    offsetY = 2.dp,
                )
                .drawDropShadow(
                    Color("#F2F2F2".toColorInt()),
                    0.7f,
                    topLeftBorderRadius = 10.dp,
                    topRightBorderRadius = 10.dp,
                    bottomLeftBorderRadius = 10.dp,
                    bottomRightBorderRadius = 10.dp,
                    shadowRadius = 2.dp,
                    offsetX = -5.dp,
                    offsetY = 0.dp,
                )
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()

            ){
                 Image(
                     painter =  rememberAsyncImagePainter(model = track.albumImageUrl),
                     contentDescription ="",
                     modifier = Modifier
                         .size(screenWidthDp - 80.dp)
                         .align(Alignment.CenterHorizontally)
                         .clip(
                             RoundedCornerShape(10.dp)
                         ),
                      contentScale = ContentScale.Crop,

                )
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Start),
                    fontSize = 17.sp,
                    text = track.name,
                    textAlign = TextAlign.Start,
                    color = Color.Gray
                )
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Start),
                    fontSize = 14.sp,
                    text = track.artistName,
                    textAlign = TextAlign.Start,
                    color = Color.Gray
                )
                Row(modifier = Modifier.fillMaxWidth()
                    .clickable {
                        externalPageNavigationController.invoke(Uri.parse(track.url))
                    },
                ){
                    Row(modifier = Modifier.weight(1f).fillMaxWidth().background(Color("#00E5FF".toColorInt())).padding(start = 15.dp)){
                        Icon(
                            imageVector = ImageVector .vectorResource(id = R.drawable.kkboxicon),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                            fontSize = 14.sp,
                            text = "KKBOX",
                            textAlign = TextAlign.Start,
                            color = Color.White
                        )
                    }
                    Row(modifier = Modifier.weight(1f).fillMaxWidth().background(Color("#FF5722".toColorInt())).padding(start =15.dp)
                        .clickable {
                            externalPageNavigationController.invoke(Uri.parse("https://m.youtube.com/results?sp=mAE&search_query=${track.artistName}+${track.name}"))

                        },
                    ){
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.youtube),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                            fontSize = 14.sp,
                            text ="Youtube",
                            textAlign = TextAlign.Start,
                            color = Color.White
                        )
                    }

                }
            }

        }
        Icon(
            modifier = Modifier
                .size(40.dp)
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .padding(3.dp)
                .clickable {
                    removeListener.invoke(track)
                },
            contentDescription = "",
            imageVector = ImageVector.vectorResource(id = R.drawable.delete),
            tint = Color.Black
        )


    }
}