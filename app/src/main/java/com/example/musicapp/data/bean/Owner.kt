package com.example.musicapp.data.bean

data class Owner(
    val id:String,
    val url:String,
    val name:String,
    val description:String,
    val images:List<Image>,
)