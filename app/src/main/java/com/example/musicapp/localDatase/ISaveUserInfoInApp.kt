package com.example.musicapp.localDatase

interface ISaveUserInfoInApp {

    fun cleanData()

    fun setToken(token:String)

    fun getToken():String

    fun setRefreshToken(refreshToken:String)

    fun getRefreshToken():String
}