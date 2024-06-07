package com.example.musicapp.viewcomponent.bottomnavigation

import com.example.musicapp.routing.Screen

data class BottomNavigationItem(
    val index: Int,
    val selectedRes: Int,
    val unSelectedRes: Int,
    val descriptionResourceId: Int,
    val screen: Screen
)
