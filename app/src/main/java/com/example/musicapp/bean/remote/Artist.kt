package com.example.musicapp.bean.remote

data  class Artist (
    val id:String,
    val name:String,
    val url:String,
    val images:List<Image>,
)
