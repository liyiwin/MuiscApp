package com.example.musicapp.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.musicapp.R
import com.example.musicapp.components.Timer
import com.example.musicapp.routing.Screen
import com.example.musicapp.theme.Theme


@Composable
fun SplashPage (navigationController:(screen: Screen)->Unit){
    Theme {
        mainContent()
    }
    Timer(2000){
        navigationController.invoke(Screen.OauthScreen())
    }
}

@Composable
private fun mainContent(){
    Column(modifier = Modifier.fillMaxSize()
    .background(
        brush = Brush.verticalGradient(
            startY = -500f,
            colors = listOf(
                Color("#00E3E3".toColorInt()),
                Color("#FFD2D2".toColorInt()),
            )
        )
    ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp).fillMaxSize(),
            alignment = Alignment.Center
        )
    }
}