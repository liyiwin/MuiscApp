package com.example.musicapp.bean.remote

data class ReLoginResult(
    val accessToken:String,
    val expiresTime:Int,
    val tokenTye:String,
    val refreshToken:String
)
