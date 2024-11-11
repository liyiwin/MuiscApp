package com.example.musicapp.page

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.TrackDetailViewModel
import com.example.musicapp.viewmodel.state.RequestState

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackDetailPage(viewModel: TrackDetailViewModel, onBackPress:()->Unit, navigationController:(screen: Screen)->Unit,externalPageNavigationController:(uri: Uri)->Unit){
    viewModel.init()
    viewModel.UpdateArtistTopTracks(RouterDataStorage.getTrack()?.album?.artist?.id?:"")

    RequestStateDialog(viewModel)

    LazyColumn(
        modifier = Modifier
            .background(Color(0xFFEEE8E8))
            .fillMaxHeight()
    ){
        item{
            TrackImageContainer(viewModel)
        }
        item{
            TrackInformationContainer(viewModel,externalPageNavigationController)
        }
        item{
            RecommendedTracksContainer(viewModel,
               OnOtherPopularTrackSelected = {
                   RouterDataStorage.putTrack(it)
                   navigationController.invoke(Screen.TrackDetailScreen())
               }
           )
        }
    }

    BackButton(onBackPress)
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestStateDialog(viewModel: TrackDetailViewModel){
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
fun BackButton(onBackPress:()->Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = { onBackPress.invoke() }
            ),
        contentAlignment = Alignment.TopStart) {
        Image(
            painter = painterResource(id = R.drawable.ic_back_white),
            contentDescription = "",
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .padding(top = 10.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackImageContainer(viewModel: TrackDetailViewModel){

    val imageUrl = viewModel.getTrackImageUrl().observeAsState()

    Surface(
        shape = RoundedCornerShape(
            0.dp,
            0.dp,
            50.dp,
            20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .drawDropShadow(
                Color.DarkGray,
                0.5f,
                bottomLeftBorderRadius = 20.dp,
                bottomRightBorderRadius = 50.dp,
                shadowRadius = 5.dp,
                offsetX = 10.dp,
                offsetY = 15.dp,
            )
    ){
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl.value?:""),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                    0.dp,
                    0.dp,
                    50.dp,
                    20.dp
                )
                )

        )
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackInformationContainer(viewModel: TrackDetailViewModel,externalPageNavigationController:(uri: Uri)->Unit){
    val title = viewModel.getTitle().observeAsState()
    val artistName = viewModel.getArtistNameOfTrack().observeAsState()
    val trackName = viewModel.getName().observeAsState()
    val url = viewModel.getUrl().observeAsState()
    val isAddedIntoFavorite = viewModel.getIsAddedIntoFavorite().observeAsState()
    Box(
        modifier = Modifier
            .padding(top =50.dp)
//              .background(Color(0xFFE7E3E3))
    ) {
        Column{
            Text(
                fontSize = 20.sp,
                text = title.value?:"",
                textAlign = TextAlign.Start,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()

            )
            Row(
                modifier = Modifier
                    .padding(top =15.dp)
            ){
                Text(
                    fontSize = 15.sp,
                    text = "歌手："+artistName.value?:"",
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .weight(3f, fill = true)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f, fill = true)
                ){
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(30.dp),
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    if(isAddedIntoFavorite.value!!) viewModel.removeFavoriteTrack() else viewModel.addFavoriteTrack()
                                }
                            ),
                        painter = painterResource(id = if(isAddedIntoFavorite.value!!) R.drawable.saved else R.drawable.not_saved),
                        contentDescription = "",

                    )
                    Text(
                        fontSize = 15.sp,
                        text = "收藏",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxHeight(),
                    )
                }

            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()

            ){
                Card(
                    shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp),
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .drawDropShadow(
                            Color.DarkGray,
                            0.5f,
                            topLeftBorderRadius = 20.dp,
                            topRightBorderRadius = 20.dp,
                            bottomLeftBorderRadius = 20.dp,
                            bottomRightBorderRadius = 20.dp,
                            shadowRadius = 5.dp,
                            offsetX = 5.dp,
                            offsetY = 5.dp,
                        )
                        .drawDropShadow(
                            Color.White,
                            0.9f,
                            topLeftBorderRadius = 20.dp,
                            topRightBorderRadius = 20.dp,
                            bottomLeftBorderRadius = 20.dp,
                            bottomRightBorderRadius = 20.dp,
                            shadowRadius = 4.dp,
                            offsetX = -5.dp,
                            offsetY = -5.dp,
                        )
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f, fill = true)
                            .background(Color(0xFFDAF8F8))
                            .padding(10.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    externalPageNavigationController.invoke(Uri.parse(url.value))
                                }
                            ),
                    ){
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(30.dp),
                            imageVector = ImageVector
                                .vectorResource(id = R.drawable.kkboxicon),
                            contentDescription = ""
                        )
                        Text(
                            fontSize = 15.sp,
                            text = "KKBox",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxHeight(),
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp),
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .drawDropShadow(
                            Color.DarkGray,
                            0.5f,
                            topLeftBorderRadius = 20.dp,
                            topRightBorderRadius = 20.dp,
                            bottomLeftBorderRadius = 20.dp,
                            bottomRightBorderRadius = 20.dp,
                            shadowRadius = 5.dp,
                            offsetX = 5.dp,
                            offsetY = 5.dp,
                        )
                        .drawDropShadow(
                            Color.White,
                            0.9f,
                            topLeftBorderRadius = 20.dp,
                            topRightBorderRadius = 20.dp,
                            bottomLeftBorderRadius = 20.dp,
                            bottomRightBorderRadius = 20.dp,
                            shadowRadius = 4.dp,
                            offsetX = -5.dp,
                            offsetY = -5.dp,
                        )
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f, fill = true)
                            .background(Color(0xFFCDEED2))
                            .padding(10.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    externalPageNavigationController.invoke(Uri.parse("https://m.youtube.com/results?sp=mAE&search_query=${artistName.value}+${trackName.value}"))
                                }
                            ),
//                          .clip(RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp))
                    ){
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(30.dp),
                            imageVector = ImageVector
                                .vectorResource(id = R.drawable.youtube),
                            contentDescription = ""
                        )
                        Text(
                            fontSize = 15.sp,
                            text = "Youtube",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxHeight(),
                        )
                    }
                }

            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecommendedTracksContainer(viewModel: TrackDetailViewModel,OnOtherPopularTrackSelected:(Track)->Unit){
    val tracks = viewModel.getTracks().observeAsState()
    Column{
        Text(
            fontSize = 20.sp,
            text = "此歌手其他熱門歌曲",
            textAlign = TextAlign.Start,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 15.dp, top = 25.dp)
        )
        LazyRow(
            modifier = Modifier.
            padding(top = 20.dp,bottom= 20.dp)
        ){
            for(element in tracks.value!!){
                item {
                    RecommendedTrack(element,OnOtherPopularTrackSelected)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecommendedTrack(track: Track,OnOtherPopularTrackSelected:(Track)->Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    OnOtherPopularTrackSelected.invoke(track)
                }
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color.Cyan)
                .align(Alignment.CenterHorizontally),
            painter = rememberAsyncImagePainter(model = track.album.images[1].url),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Text(
            fontSize = 15.sp,
            text = track.name,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp))
    }
}