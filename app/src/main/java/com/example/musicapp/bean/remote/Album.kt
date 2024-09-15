package com.example.musicapp.bean.remote

data class Album (
    val id:String,
    val name:String,
    val url:String,
    val explicitness:Boolean,
    val available_territories:List<String>,
    val release_date:String,
    val images:List<Image>,
    val artist: Artist
)