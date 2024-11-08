package com.example.musicapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun Timer(timeMillis: Long, onTimeUp: () -> Unit) {
    LaunchedEffect(true) {
        delay(timeMillis)
        onTimeUp()
    }
}
