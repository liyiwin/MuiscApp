package com.example.musicapp.viewcomponent.bottomnavigation

import androidx.compose.ui.graphics.Color
import com.example.musicapp.routing.Screen

data class BottomNavigationItem(
    val index: Int,
    val selectedRes: Int,
    val unSelectedRes: Int,
    val background: Color,
    val descriptionResourceId: Int,
    val screen: Screen
)
