package com.example.musicapp.textConverter

import android.net.Uri
import android.util.Log

object OauthDataConverter {

    fun convertAuthCode(url: Uri):String?{
        var authCode :String ? = null
        val startregex = """^https\:\/\/musicapp.com\/\?code\=""".toRegex()
        val endregex = """\&state\=TestState$""".toRegex()
        val urlString = url.toString()
        if(urlString.contains(startregex) && urlString.contains(endregex) ){
             authCode = urlString.replace("https://musicapp.com/?code=","").replace("&state=TestState","")
        }
        return authCode
     }

}