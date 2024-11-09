package com.example.musicapp.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.kkbox_music_app.components.dialog.FailureDialog
import com.example.musicapp.R
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.theme.Theme
import com.example.musicapp.viewmodel.LoginViewModel
import com.example.musicapp.viewmodel.state.RequestState
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginPage (loginViewModel: LoginViewModel, navigationController:(screen: Screen)->Unit){
    loginViewModel.triggerLogin(RouterDataStorage.getAuthCode())
    RequestStateDialog(loginViewModel,navigationController)
    Theme {
        mainContent()
    }

}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestStateDialog(viewModel: LoginViewModel, navigationController:(screen: Screen)->Unit){
    val requestState = viewModel.getRequestState().observeAsState()
    requestState.value?.let {
        when(it){

            is RequestState.Loading ->{

            }

            is RequestState.Success ->{
                viewModel.cleanRequestState()
                navigationController.invoke(Screen.Home())
            }

            is RequestState.Failure ->{
                FailureDialog(title = "加載失敗", message =it.message, modifier = Modifier){
                    viewModel.cleanRequestState()
                    viewModel.triggerLogin(RouterDataStorage.getAuthCode())
                }
            }

            is RequestState.None->{

            }
   }
    };
}

@Composable
private fun mainContent(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    startY = -500f,
                    colors = listOf(
                        Color("#2BC0E4".toColorInt()),
                        Color("#EAECC6".toColorInt()),
//                        Color("#6DD5FA".toColorInt()),

                    )
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
            painter = rememberDrawablePainter(
                drawable = getDrawable(
                    LocalContext.current,
                    R.drawable.loading
                )
            ),
            contentDescription = "",
            modifier = Modifier.size(150.dp),
            alignment = Alignment.TopCenter
        )
        Text(
            fontSize = 15.sp,
            text = "資料設定中，請稍後......",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 25.dp),


        )
    }
}