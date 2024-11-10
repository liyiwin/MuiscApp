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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
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
import com.example.musicapp.components.drawInnerShadow
import com.example.musicapp.components.isScrolledToEnd
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.SearchViewModel
import com.example.musicapp.viewmodel.state.RequestState

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchPage(viewModel: SearchViewModel, navigationController:(screen:Screen)->Unit){

    RequestStateDialog(viewModel)
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color("#FFFDF6".toColorInt()))
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))


    ){
        SearchTextField(viewModel)
        SearchResultContainer(viewModel){
            RouterDataStorage.putTrack(it)
            navigationController.invoke(Screen.TrackDetailScreen())
        }
    }
}


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestStateDialog(viewModel: SearchViewModel){
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchTextField(viewModel:SearchViewModel){
    var textValue by remember { mutableStateOf(TextFieldValue("")) }
    Surface(
        shape  = RoundedCornerShape(10.dp),
        color = Color(0xFFADADAD),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 10.dp, end = 10.dp)
            .drawDropShadow(
                Color.DarkGray,
                0.7f,
                topLeftBorderRadius = 10.dp,
                topRightBorderRadius = 10.dp,
                bottomLeftBorderRadius = 10.dp,
                bottomRightBorderRadius = 10.dp,
                shadowRadius =5.dp,
                offsetX = 0.dp,
                offsetY = 2.dp,
            ),
    ){
        TextField(
            value = textValue,
            trailingIcon = {
                Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = "",
                modifier = Modifier. clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        viewModel.reset()
                        viewModel.searchTrack(textValue.text)
                    }
                )

            ) },
            modifier = Modifier .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color("#FFFDF6".toColorInt()),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = { newText ->
                textValue = newText
            }
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchResultContainer(viewModel:SearchViewModel,itemSelected:(Track)->Unit){
    val searchResult = viewModel.getSearchResult().observeAsState()
    val listState = rememberLazyListState()
    val endOfListReached by remember { derivedStateOf { listState.isScrolledToEnd() } }
    Column(modifier =   Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ){
        Text(
            fontSize = 15.sp,
            text = "搜尋結果",
            textAlign = TextAlign.Start,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 25.dp)

        )
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 15.dp, top = 25.dp, bottom = 30.dp)
        ){
            for(element in searchResult.value!!){
                item{
                    SearchResultItem(element){
                        itemSelected.invoke(it)
                    }
                }
            }
        }
    }
    LaunchedEffect(endOfListReached){
        if(endOfListReached) viewModel.LoadSearchResult()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchResultItem(track: Track,itemSelected:(Track)->Unit){
    Card(
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top =5.dp, bottom =5.dp, start =5.dp, end =15.dp)
            .drawDropShadow(
                Color.DarkGray,
                0.7f,
                topLeftBorderRadius = 20.dp,
                topRightBorderRadius =20.dp,
                bottomLeftBorderRadius = 20.dp,
                bottomRightBorderRadius = 20.dp,
                shadowRadius =5.dp,
                offsetX = 0.dp,
                offsetY = 2.dp,
            )
            .clickable {
                itemSelected.invoke(track)
            }
    ){

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color("#FFFAED".toColorInt()))
                .padding(20.dp)
        ){

            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.Cyan)
                    .align(Alignment.CenterVertically),
                painter = rememberAsyncImagePainter(model = track.album.images.get(1).url),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp)
                    .weight(3f, fill = true)
            ){
                Column(
                ){
                    Text(
                        fontSize = 15.sp,
                        text = track.name,
                        textAlign = TextAlign.Start,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)

//                      ,
                    )

                    Text(
                        fontSize = 15.sp,
                        text = "歌手: "+track.album.artist.name,
                        textAlign = TextAlign.Start,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Start)


                    )
                }
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
                    .weight(1f, fill = true)
                    .size(15.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_right_arrow),
                contentDescription = ""
            )
        }
    }
}