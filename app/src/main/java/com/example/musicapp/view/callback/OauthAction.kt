package com.example.musicapp.view.callback

interface OauthAction {

    fun navigationToLogin(code:String)

    fun dealConnectError()

}