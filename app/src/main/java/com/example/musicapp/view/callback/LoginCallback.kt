package com.example.musicapp.view.callback

interface LoginCallback {
    fun onStart()

    fun onError()

    fun onSuccess(authCode:String)
}