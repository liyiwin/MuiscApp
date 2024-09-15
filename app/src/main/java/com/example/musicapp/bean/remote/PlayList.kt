package com.example.musicapp.bean.remote

import com.example.musicapp.bean.remote.Image
import com.example.musicapp.bean.remote.Owner

data class PlayList (
    val id:String,
    val title:String,
    val description:String,
    val url:String,
    val images:ArrayList<Image>,
    val updated_at:String,
    val owner: Owner,
)