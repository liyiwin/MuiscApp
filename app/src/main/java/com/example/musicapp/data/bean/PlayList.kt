package com.example.musicapp.data.bean

data class PlayList (
    val id:String,
    val title:String,
    val description:String,
    val url:String,
    val images:ArrayList<Image>,
    val updated_at:String,
    val owner:Owner,
)