package com.example.musicapp.bean.remote

import com.example.musicapp.bean.remote.Image

data class Owner(
    val id:String,
    val url:String,
    val name:String,
    val description:String,
    val images:List<Image>,
)