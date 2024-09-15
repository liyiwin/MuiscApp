package com.example.musicapp.bean.remote

data class Track (
    val id:String,
    val name:String,
    val duration:Int,
    val isrc:String,
    val url:String,
    val track_number:Int,
    val explicitness:Boolean,
    val available_territories:List<String>,
    val album:Album
)