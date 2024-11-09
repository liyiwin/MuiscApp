package com.example.musicapp.bean.remote

data class LoginResult(
    val accessToken:String,
    val expiresTime:Int,
    val tokenTye:String,
    val refreshToken:String
)
