package com.example.musicapp.tool.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun getScreenWidth(): Dp {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = configuration.screenWidthDp // 螢幕寬度（dp）
    return with(density) { screenWidthPx.dp } // 轉換為 dp// 螢幕寬度（dp）
}