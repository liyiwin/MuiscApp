package com.example.musicapp.bean.remote

data class PersonalInformation (
    val id:String,
    val url:String,
    val name:String,
    val description:String,
    val images:List<Image>
)