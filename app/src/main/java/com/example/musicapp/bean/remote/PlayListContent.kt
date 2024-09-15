package com.example.musicapp.bean.remote

data class PlayListContent(
    val tracks:List<Track>,
    val id:String,
    val title:String,
)